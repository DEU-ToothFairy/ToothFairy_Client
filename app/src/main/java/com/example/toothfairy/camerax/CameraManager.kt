package com.example.toothfairy.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.ScaleGestureDetector
import androidx.camera.core.*
import androidx.camera.core.CameraSelector.LensFacing
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.toothfairy.mlkit.FaceContourDetectionProcessor
import com.example.toothfairy.viewModel.FaceDetectViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context: Context,
    private val finderView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val graphicOverlay: GraphicOverlay,
    private val faceVM:FaceDetectViewModel
) {

    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null

    // default barcode scanner
//    private var analyzerVisionType: VisionType = VisionType.Barcode

    lateinit var cameraExecutor: ExecutorService
    lateinit var imageCapture: ImageCapture
    lateinit var metrics: DisplayMetrics

    var rotation: Float = 0f
    var cameraSelectorOption = CameraSelector.LENS_FACING_BACK

    init {
        createNewExecutor()
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

//    private fun selectAnalyzer(): ImageAnalysis.Analyzer {
//        return when (analyzerVisionType) {
//            VisionType.Object -> ObjectDetectionProcessor(graphicOverlay)
//            VisionType.OCR -> TextRecognitionProcessor(graphicOverlay)
//            VisionType.Face -> FaceContourDetectionProcessor(graphicOverlay)
//            VisionType.Barcode -> BarcodeScannerProcessor(graphicOverlay)
//        }
//    }

    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider?,
        cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(
                finderView.surfaceProvider
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPinchToZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }
        val scaleGestureDetector = ScaleGestureDetector(context, listener)
        finderView.setOnTouchListener { _, event ->
            finderView.post {
                scaleGestureDetector.onTouchEvent(event)
            }
            return@setOnTouchListener true
        }
    }

    fun startCamera(isFrontFace:Boolean) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            Runnable {
                cameraProvider = cameraProviderFuture.get()
                preview = Preview.Builder().build()

                /**
                 * ImageAnalzer로 FaceContourDetectionProcessor가 들어감
                 * FaceCountourDetectionProcessor 안에 FaceContourGraphinc 클래스가 Canvas로 그려줌
                 * Draw 메소드 안에서 Face 좌표 출력 가능
                 * graphicOverlay를 인자로 전달해서 그 위에 그려줌
                 */
                imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        // 정면 얼굴 촬영할 때만 ML Kit 동작하도록
                        if(isFrontFace) it.setAnalyzer(cameraExecutor, FaceContourDetectionProcessor(graphicOverlay, faceVM))
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraSelectorOption.apply {
                        if (isFrontFace) CameraSelector.LENS_FACING_FRONT
                        else CameraSelector.LENS_FACING_BACK}
                    )
                    .build()

                metrics =  DisplayMetrics().also { finderView.display.getRealMetrics(it) }

                /**
                 * ImageCapture 빌더 Size가 폰 크기로 되어있음
                 * 이걸 View 크기로 맞추면 될지도
                 */
                imageCapture =
                    ImageCapture.Builder()
                        .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                        .build()

                setUpPinchToZoom()
                setCameraConfig(cameraProvider, cameraSelector)

            }, ContextCompat.getMainExecutor(context)
        )
    }

    fun changeCameraSelector(isFrontFace: Boolean) {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
            if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        graphicOverlay.toggleSelector()

        startCamera(isFrontFace)
    }

//    fun changeAnalyzer(visionType: VisionType) {
//        if (analyzerVisionType != visionType) {
//            cameraProvider?.unbindAll()
//            analyzerVisionType = visionType
//            startCamera()
//        }
//    }

    fun isHorizontalMode() : Boolean {
        return rotation == 90f || rotation == 270f
    }

    fun isFrontMode() : Boolean {
        return cameraSelectorOption == CameraSelector.LENS_FACING_FRONT
    }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private const val TAG = "CameraXBasic"
    }

}
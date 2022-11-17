package com.example.toothfairy.mlkit

import android.graphics.Rect
import android.util.Log
import com.example.toothfairy.camerax.BaseImageAnalyzer
import com.example.toothfairy.camerax.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException

class FaceContourDetectionProcessor(private val view: GraphicOverlay) :
    BaseImageAnalyzer<List<Face>>() {

    /**
     * 실시간으로 얼굴 감지해서 계속 찍음 (onSuccess 로그 찍어보기)
     * 실시간으로 찍는거 말고 사진 찍었을 때만 찍게 변경하기
     */
    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(realTimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    /**
     * 얼굴 감지 실행
     */
    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    override fun onSuccess(results: List<Face>,graphicOverlay: GraphicOverlay,rect: Rect) {
        graphicOverlay.clear()
        results.forEach {
            val faceGraphic = FaceContourGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(faceGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

}
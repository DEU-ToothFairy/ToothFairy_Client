package com.example.toothfairy.view.fragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.application.MyApplication
import com.example.toothfairy.camerax.CameraManager
import com.example.toothfairy.databinding.FragmentToothBrushCameraXBinding
import com.example.toothfairy.util.Extention.hideBottomNabBar
import com.example.toothfairy.util.Extention.hideTitleBar
import com.example.toothfairy.util.getBaseYByView
import com.example.toothfairy.util.imageToBitmap
import com.example.toothfairy.util.rotateFlipImage
import com.example.toothfairy.util.scaleImage
import com.example.toothfairy.viewModel.FaceDetectViewModel
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ToothBrushCameraXFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ToothBrushCameraXFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentToothBrushCameraXBinding
    private lateinit var cameraManager: CameraManager
    private lateinit var faceVM: FaceDetectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        faceVM = ViewModelProvider(this)[FaceDetectViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_tooth_brush_camera_x, container, false)

        this.apply {
            hideTitleBar()
            hideBottomNabBar()
        }

        // 카메라 매니저 생성
        createCameraManager()

        if (allPermissionsGranted()) {
            cameraManager.startCamera(false)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addBtnClickEvent()

        return bind.root
    }

    private fun addBtnClickEvent() {

        //bind.fabFinder.transform()
        // 카메라 전환 버튼 이벤트
        bind.cameraChangeBtn.setOnClickListener{
            cameraManager.changeCameraSelector(false)
        }

        // 촬영 버튼 클린 이벤트
        bind.button.setOnClickListener {
            takePicture()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults:IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera(false)
            } else {
                Toast.makeText(requireContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                //finish()
            }
        }
    }

    // 카메라 매니저 생성 메소드
    private fun createCameraManager() {
        cameraManager = CameraManager(
            requireContext(),
            bind.previewViewFinder,
            this,
            bind.graphicOverlayFinder,
            faceVM // FaceDetectViewModel를 CameraManager에 전달 (FaceContourDetectionProcessor에게 전달하기 위함)
        )
    }

    // 사진 촬영 메소드
    private fun takePicture() {
        // shutter effect
        Toast.makeText(requireContext(), "take a picture!", Toast.LENGTH_SHORT).show()
        setOrientationEvent()
        cameraManager.imageCapture.takePicture(
            cameraManager.cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi")
                @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    image.image?.let {
                        // 이미지를 갤러리에 저장하는 메소드
                        imageToBitmapSaveGallery(it)
                    }
                    super.onCaptureSuccess(image)
                }
            })
    }

    // 이미지를 비트맵으로 갤러리에 저장
    private fun imageToBitmapSaveGallery(image: Image) {
        image.imageToBitmap()
            ?.rotateFlipImage(
                cameraManager.rotation,
                cameraManager.isFrontMode()
            )?.scaleImage(
                bind.previewViewFinder,
                cameraManager.isHorizontalMode()
            )?.let { bitmap ->
                bind.graphicOverlayFinder.processCanvas.drawBitmap(
                    bitmap,
                    0f,
                    bitmap.getBaseYByView(
                        bind.graphicOverlayFinder,
                        cameraManager.isHorizontalMode()
                    ),
                    Paint().apply {
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                    })
                //bind.graphicOverlayFinder.processBitmap.saveToGallery(requireActivity(), faceVM)

                /**
                 * 여기서 비트맵을 서버로 전송
                 * 비트맵 용량 줄여서 전송
                 */
                val outStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream)
                // byteArray로 용량 줄여진걸 다시 비트맵으로 변환

                val compressBitmap = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.toByteArray().size)
                MyApplication.patient?.let {
                    faceVM.detectToothBrush(it.patientId?:"00000000", compressBitmap)

                    // 로딩 페이지로 이동
                    moveToBrushLoadingFragment()
                }
            }
    }


    /**
     * childFragmentManager를 통해 로딩 프래그먼트로 이동하는 메소드
     * childFragmentManager를 사용할 땐 ParentFragment의 전체 레이아웃을 넣어줘야함
     * (그 위에 프래그먼트가 띄워질 것이기 때문)
     */
    fun moveToBrushLoadingFragment(){
        childFragmentManager
            .beginTransaction()
            .replace(R.id.toothBrushFrameLayout, BrushLoadingFragment())
            .commit()
    }

    // 회전 이벤트 ?
    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager.rotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    // 퍼미션 얻는 메소드
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SideFaceCameraXFragment.
         */
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToothBrushCameraXFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
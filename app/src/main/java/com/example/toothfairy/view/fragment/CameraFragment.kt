package com.example.toothfairy.view.fragment

import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentCameraBinding
import com.example.toothfairy.view.customview.CameraSurfaceView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentCameraBinding
    private var cameraFacing:Int = Camera.CameraInfo.CAMERA_FACING_FRONT
    var image:InputImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.GONE

        faceDetection()
        initFragment()

        return bind.root
    }

    private fun faceDetection(){
        // High-accuracy landmark detection and face classification
        // detector에 대한 옵션 설정
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        // detector생성
        val detector: FaceDetector = FaceDetection.getClient(highAccuracyOpts)
        
        // GET IMAGE 버튼
        bind.imageView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 1)
        })

//        // 얼굴 인식 버튼
//        bind.detectionBtn.setOnClickListener {
//            val result: Task<List<Face>> =
//                image?.let {
//                    detector.process(image!!)
//                        .addOnSuccessListener(object : OnSuccessListener<MutableList<Face>> {
//                            override fun onSuccess(faces: MutableList<Face>?) {
//                                Log.i("얼굴 인식 정보", faces.toString())
//                            }
//                        })
//                        .addOnFailureListener(object : OnFailureListener {
//                            override fun onFailure(p0: Exception) {
//                                Log.i("얼굴 인식 실패", "실패")
//                            }
//                        })
//                } as Task<List<Face>>
//        }
    }

    private fun initFragment(){
        bind.button.setOnClickListener{ capture() }
        /**
         * SurfaceView를 터치하면 오토포커스 작동
         */
        bind.surfaceView.setOnClickListener{
            bind.surfaceView.autoFocus { _, _ -> } // camera 객체의 autuFoucs를 호출할 때 실행할 행동을 callback 메소드로 구현하는건데 할 일 없으면 null로 넣어도 됨
        }

        bind.cameraChangeBtn.setOnClickListener{
            bind.surfaceView.apply {
                surfaceDestroyed(this.surfaceHolder)
                destroyDrawingCache()
                holder.removeCallback(this)
            }

            cameraFacing =
                if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) Camera.CameraInfo.CAMERA_FACING_FRONT
                else Camera.CameraInfo.CAMERA_FACING_BACK

            // 변경된 방향으로 새로운 카메라 View 생성
            val surfaceView = CameraSurfaceView(requireContext(), cameraFacing)
            surfaceView.layoutParams = FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )

            bind.surfaceLayout.apply {
                removeAllViews()
                addView(surfaceView)
            }

            bind.button.setOnClickListener(null)
            bind.surfaceView.setOnClickListener(null)
            bind.cameraChangeBtn.setOnClickListener(null)

            // View를 재생성 시키는 것이기 때문에 기존에 등록했던 이벤트 리스너들이 날아가므로
            // 프래그먼트 전체의 초기화 코드를 initFragment()으로 뺴서 재호출
            initFragment()
        }
    }

    /**
     * 갤러리에서 가져온 이미지를 받을 메소드
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다.
            val uri = data?.data;
            uri?.let {
                setImage(it)
            }
        }

    }


    /**
     * uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
     */
    private fun setImage(uri: Uri) {
        try {
            val `in`: InputStream = requireActivity().contentResolver.openInputStream(uri)!!
            val bitmap = BitmapFactory.decodeStream(`in`)

            bind.imageView.setImageBitmap(bitmap)

            image = InputImage.fromBitmap(bitmap, 0)

            Log.e("setImage", "이미지 to 비트맵")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

//    @SuppressLint("SdCardPath")
//    private fun capture(){
//        /**
//         * 이미지가 바이트 배열로 data에 저장 됨
//         */
//        bind.surfaceView.capture()
//
//        Log.i("찍힌 이미지", bind.surfaceView.image.toString())
//    }

    /**
     * 사진 찍는 메소드
     */
    private fun capture() {
        /**
         * 사진이 ByteArray로 data에 들어옴
         */
        bind.surfaceView.capture { data, camera ->
            camera.startPreview() // 카메라를 다시 시작
            activity?.let {
                val fragment = SideFaceResultFragment()
                fragment.arguments = Bundle().apply {
                    putByteArray("image", data)
                }

                it.supportFragmentManager.beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.VISIBLE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
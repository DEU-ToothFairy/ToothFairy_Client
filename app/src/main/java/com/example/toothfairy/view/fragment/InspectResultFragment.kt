package com.example.toothfairy.view.fragment

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentInspectResultBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InspectResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InspectResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var image: ByteArray? = null
    private lateinit var bind:FragmentInspectResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getByteArray("image")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_inspect_result, container, false)

        makeBitmap()
        return bind.root
    }

    /**
     * [ 구글 버전 ] Bitmap 생성 & 리사이징
     */
    private fun makeBitmap(){
        image?.let{
            //data 어레이 안에 있는 데이터 불러와서 비트맵에 저장
            val bitmap = BitmapFactory.decodeByteArray(image, 0, image!!.size)

            val width = bitmap.width
            val height = bitmap.height
            val newWidth = 1000
            val newHeight = 1000

            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height

            val matrix = Matrix().apply {
                postScale(scaleWidth, scaleHeight)
                postRotate(-90F)
            }

            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
            val bmd = BitmapDrawable(resizedBitmap)

            bind.imageView.setImageDrawable(bmd) //이미지뷰에 사진 보여주기

            saveBitmap(resizedBitmap)
            /**
             * 리사이징 된 비트맵으로 faceDetection
             */
            faceDetection(InputImage.fromBitmap(resizedBitmap, 0))
        }
    }

    private fun saveBitmap(bitmap: Bitmap){
        var back = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        var canvas = Canvas(back)
        var paint = Paint()
        paint.textSize = 20F
        canvas.drawBitmap(bitmap, 0f,0f,null)
        canvas.drawText("테스트 텍스트", 20f, paint.measureText("yY") + 20f, paint)

        val path = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$path/ToothFairy")

        if(!myDir.exists()){
            myDir.mkdirs()
        }

        try{
            back.compress(Bitmap.CompressFormat.PNG, 100,
                FileOutputStream("${File(myDir.path)}/${System.currentTimeMillis()}.png"))
        } catch (e: FileNotFoundException){
            e.printStackTrace()
        }
    }
    /**
     * [ 구글버전 ] 안면 인식 메소드
     */
    private fun faceDetection(inputImage: InputImage){
        // High-accuracy landmark detection and face classification
        // detector에 대한 옵션 설정
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()


        // GET IMAGE 버튼
        bind.imageView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 1)
        })

        // detector생성
        val detector: FaceDetector = FaceDetection.getClient(highAccuracyOpts)

        // 얼굴 인식 버튼
        val result: Task<List<Face>> =
            image?.let {
                detector.process(inputImage)
                    .addOnSuccessListener(object : OnSuccessListener<MutableList<Face>> {
                        override fun onSuccess(faces: MutableList<Face>?) {
                            Log.i("얼굴 인식 정보", faces.toString())
                            if (faces != null) {
                                for (face in faces) {
                                    val bounds = face.boundingBox
                                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                    // nose available):
                                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                                    leftEar?.let {
                                        val leftEarPos = leftEar.position
                                    }

                                    // If contour detection was enabled:
                                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                                    val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                                    // If classification was enabled:
                                    if (face.smilingProbability != null) {
                                        val smileProb = face.smilingProbability
                                    }
                                    if (face.rightEyeOpenProbability != null) {
                                        val rightEyeOpenProb = face.rightEyeOpenProbability
                                    }

                                    // If face tracking was enabled:
                                    if (face.trackingId != null) {
                                        val id = face.trackingId
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(p0: Exception) {
                            Log.i("얼굴 인식 실패", "실패")
                        }
                    })
            } as Task<List<Face>>
    }

    /**
     * ByteArray를 InputImage로 만들어주는 메소드
     */
    private fun makeInputImage(data:ByteArray): InputImage{
        return InputImage.fromByteArray(
            data,
            /* image width */ 480,
            /* image height */ 360,
            -90,
            InputImage.IMAGE_FORMAT_NV21
        )
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InspectResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InspectResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
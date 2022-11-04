package com.example.toothfairy.view.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.toothfairy.R
import com.example.toothfairy.databinding.FragmentCameraBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


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

    // VARIABLE
    private lateinit var bind:FragmentCameraBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        bind.button.setOnClickListener{ capture() }

        /**
         * SurfaceView를 터치하면 오토포커스 작동
         */
        bind.surfaceView.setOnClickListener{
            bind.surfaceView.autoFocus { _, _ -> }
        }

        return bind.root
    }

//    private fun capture(){
//        bind.surfaceView.capture { data, camera ->
//            //bytearray 형식으로 전달
//            //이걸이용해서 이미지뷰로 보여주거나 파일로 저장
//            val options = BitmapFactory.Options().apply {
//                inSampleSize = 8 // 1/8사이즈로 보여주기
//            }
//
//            //data 어레이 안에 있는 데이터 불러와서 비트맵에 저장
//            val bitmap = BitmapFactory.decodeByteArray(data,0, data.size)
//
//            val width = bitmap.width
//            val height = bitmap.height
//            val newWidth = 200
//            val newHeight = 200
//
//            val scaleWidth = newWidth.toFloat() / width
//            val scaleHeight = newHeight.toFloat() / height
//
//            val matrix = Matrix().apply {
//                postScale(scaleWidth, scaleHeight)
//                postRotate(90F)
//            }
//
//            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
//            val bmd = BitmapDrawable(resizedBitmap)
//
//            bind.imageView.setImageDrawable(bmd) //이미지뷰에 사진 보여주기
//            camera.startPreview()
//        }
//    }

    @SuppressLint("SdCardPath")
    private fun capture(){
        /**
         * 이미지가 바이트 배열로 data에 저장 됨
         */
        bind.surfaceView.capture{ data, camera ->
            var photoPath:String? = null

            try {
                // 이미지를 파일로 저장
                photoPath = String.format("/sdcard/DCIM/Pictures/%d.jpg",System.currentTimeMillis())

                FileOutputStream(photoPath).apply {
                    write(data)
                    close()
                }
            }
            catch (e: FileNotFoundException) { e.printStackTrace()}
            catch (e: IOException) { e.printStackTrace() }

            Toast.makeText(requireContext(),"Picture Saved", Toast.LENGTH_LONG).show()

            /**
             * 바이트 배열에 담긴 이미지를 비트맵으로 변경 후
             * 비트맵을 이미지뷰로 출력하는 코드
             * 
             * val options = BitmapFactory.Options().apply { inSampleSize = 4 } // inSampleSize를 줄이면 화질 좋아짐
             * val bmp = BitmapFactory.decodeFile(photoPath, options)
             * val matrix = Matrix().apply { preRotate(90F) }
             *
             * val adjustBitmap = Bitmap.createBitmap(bmp, 0,0, bmp.width, bmp.height, matrix, true)
             * bind.imageView.setImageBitmap(adjustBitmap)
             */

            camera.startPreview()
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
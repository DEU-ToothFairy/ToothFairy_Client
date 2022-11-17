package com.example.toothfairy.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.hardware.Camera.PictureCallback
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.example.toothfairy.view.fragment.CameraFragment
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CameraSurfaceView : SurfaceView, SurfaceHolder.Callback {
    lateinit var surfaceHolder: SurfaceHolder
    var camera: Camera? = null
    var cameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_FRONT // 전면 or 후면 카메라 상태 저장 (초기값 전면)
    var image:ByteArray? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, cameraFacing: Int) : super(context){
        this.cameraFacing = cameraFacing
        init(context)
    }

    private fun init(context: Context) {
        //초기화를 위한 메소드
        surfaceHolder = holder
        surfaceHolder.addCallback(this)

        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }

    /**
     * surface가 생성되었을 때 할 작업. Camera를 불러오고 미리보기 화면을 surface로 지정함.
     */
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        //만들어지는시점
        camera = Camera.open(cameraFacing) //카메라 객체 참조

        try {
            camera?.setPreviewDisplay(holder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * surface가 바뀌었을 때 할 작업. Camera 미리보기를 실행함.
     */
    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        //변경
        camera?.apply {
            startPreview() //렌즈로 부터 들어오는 영상을 뿌려줌
            stopPreview()
            setDisplayOrientation(90) //카메라 미리보기 오른쪽 으로 90 도회전
            startPreview()
        }
    }

    /**
     * surface가 사라졌을 때 할 작업. Camera 미리보기를 멈추고 리소스를 해제함.
     */
    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        //소멸
        camera?.apply { 
            stopNestedScroll()
            release() // 미리보기 중지
        }

        camera = null
    }


    fun capture(): Boolean {
        return if (camera != null) {
            // imageView에 사진을 출력하고 싶으면 fragment쪽에서 callback을 구현해서 인자에 추가로 넣어주면 됨
            camera!!.takePicture(null, null, null, jpegCallback)
            true
        } else {
            false
        }
    }

    fun capture(saveCallback:PictureCallback): Boolean{
        return if(camera != null){
            camera!!.takePicture(null, null, saveCallback)
            true
        } else {
            false
        }
    }

    fun autoFocus(callback:AutoFocusCallback): Boolean{
        return if (camera != null) {
            camera!!.autoFocus(callback)
            true
        } else {
            false
        }
    }

    
    @SuppressLint("SdCardPath")
    private val jpegCallback = PictureCallback { data, camera ->
        var photoPath:String? = null

        try {
            // 이미지를 파일로 저장
            photoPath = String.format("/sdcard/DCIM/Pictures/%d.jpg", System.currentTimeMillis())
            this.image = data

            FileOutputStream(photoPath).apply {
                write(data)
                close()
            }
        }
        catch (e: FileNotFoundException) { e.printStackTrace()}
        catch (e: IOException) { e.printStackTrace() }

        Toast.makeText(context,"Picture Saved", Toast.LENGTH_LONG).show()

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
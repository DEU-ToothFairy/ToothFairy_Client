package com.example.toothfairy.view.customview

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView


class CameraSurfaceView : SurfaceView, SurfaceHolder.Callback {
    lateinit var surfaceHolder: SurfaceHolder
    var camera: Camera?

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        //초기화를 위한 메소드
        surfaceHolder = getHolder()
        surfaceHolder.addCallback(this)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        //만들어지는시점
        camera = Camera.open() //카메라 객체 참조

        try {
            camera?.setPreviewDisplay(surfaceHolder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        //변경
        camera?.let {
            it.startPreview() //렌즈로 부터 들어오는 영상을 뿌려줌
            it.stopPreview()
            it.setDisplayOrientation(90) //카메라 미리보기 오른쪽 으로 90 도회전
            it.startPreview()

        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        //소멸
        camera.stopPreview() //미리보기중지
        camera.release()
        camera = null
    }

    fun capture(callback: Camera.PictureCallback?): Boolean {
        return if (camera != null) {
            camera!!.takePicture(null, null, callback)
            true
        } else {
            false
        }
    }
}
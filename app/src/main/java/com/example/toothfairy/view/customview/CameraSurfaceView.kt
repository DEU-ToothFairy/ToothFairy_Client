package com.example.toothfairy.view.customview

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.hardware.Camera.PictureCallback
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class CameraSurfaceView : SurfaceView, SurfaceHolder.Callback {
    lateinit var surfaceHolder: SurfaceHolder
    var camera: Camera? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
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
        camera = Camera.open() //카메라 객체 참조

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

    fun capture(callback: PictureCallback?): Boolean {
        return if (camera != null) {
            camera!!.takePicture(null, null, callback)
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
}
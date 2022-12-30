package com.example.toothfairy.application

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.toothfairy.entity.Patient
import com.example.toothfairy.util.NotifyManager
import java.sql.Date
import java.util.Calendar

/** Context를 전역적으로 받기 위한 클래스 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        NotifyManager.createNotificationChannel(
            applicationContext,
            NotifyManager.WEARING_NOTIFY_ID,
            "교정기 착용 알림"
        )

        NotifyManager.createNotificationChannel(
            applicationContext,
            NotifyManager.WEAR_RECOMMEND_NOTIFY_ID,
            "교정기 착용 권장 알림"
        )
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        // 필요한 권한 목록
        val REQUIRED_PERMISSIONS:Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES,
        )

        val REQUIRED_CAMERA_PERMISSIONS: Array<String> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU == Android 13
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

        /** 사용자 정보 */
        var patient:Patient? = null

        @JvmStatic
        fun ApplicationContext(): Context? {
            return context
        }
    }
}

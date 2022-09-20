package com.example.toothfairy.util

import android.app.Application
import android.content.Context
import com.example.toothfairy.util.MyApplication

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

    companion object{
        private var context: Context? = null

        @JvmStatic
        fun ApplicationContext(): Context? {
            return context
        }
    }

}
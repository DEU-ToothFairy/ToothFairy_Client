package com.example.toothfairy.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.toothfairy.DateChangedReceiver
import com.example.toothfairy.R
import com.example.toothfairy.view.activity.MainActivity

object NotifyManager {
    private lateinit var notificationManager: NotificationManager

    /** 알림 전송 */
    fun sendNotification(context: Context, title: String, content: String) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent, // 알림 클릭시 이동할 인텐트
            PendingIntent.FLAG_IMMUTABLE
        )

        // Notivication에 대한 ID 생성
        val notifyBuilder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentPendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(R.drawable.appnamelogo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    /** 알림 채널 생성 */
    fun createNotificationChannel(context: Context) {
        if(!::notificationManager.isInitialized)
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel 정의 생성자( construct 이용 )
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "ToothFairy Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from ToothFairy"

            // Manager을 이용하여 Channel 생성
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    const val TAG = "AlarmReceiver"
    const val NOTIFICATION_ID = 0
    const val PRIMARY_CHANNEL_ID = "ToothFairy"
}
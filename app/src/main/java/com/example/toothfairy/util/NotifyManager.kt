package com.example.toothfairy.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.toothfairy.R

object NotifyManager {
    private lateinit var notificationManager: NotificationManager

    /**
     * 알림 전송
     * @param context: Context
     * @param channelId: String
     * @param title: String
     * @param content: String
     * */
    fun sendNotification(context: Context, channelId: String, title: String, content: String) {
//        val contentPendingIntent = PendingIntent.getActivity(
//            context,
//            NOTIFICATION_ID, // requestCode
//            null, // 알림 클릭시 이동할 인텐트
//            PendingIntent.FLAG_IMMUTABLE
//        )

        // Notivication에 대한 ID 생성
        val notifyBuilder =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(R.drawable.appnamelogo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notificationManager.notify(System.currentTimeMillis().toInt(), notifyBuilder.build())
    }

    /**
     *  알림 채널 생성
     *  @param context: Context
     *  @param channelId: String
     *  @param notifyName: String
     * */
    fun createNotificationChannel(context: Context, channelId:String, notifyName:String) {
        if(!::notificationManager.isInitialized)
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel 정의 생성자( construct 이용 )
            val notificationChannel = NotificationChannel(
                channelId,
                notifyName,
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

    const val PRIMARY_CHANNEL_ID = "ToothFairy"
    const val WEARING_NOTIFY_ID = "Wearing Notification"              // 교정기 착용 알림 채널
    const val WEAR_RECOMMEND_NOTIFY_ID = "Wearing Time Notification"  // 교정기 착용 권장 알림 채널
}
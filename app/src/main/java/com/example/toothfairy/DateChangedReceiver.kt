package com.example.toothfairy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/** 날짜 변경을 감지하는 리시버 클래스 */
class DateChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        com.example.toothfairy.util.NotifyManager.createNotificationChannel(context)
        com.example.toothfairy.util.NotifyManager.sendNotification(context, "착용 시간 알림", "착용 시간")
        Toast.makeText(context, "알림 울림", Toast.LENGTH_LONG).show()

    }





}
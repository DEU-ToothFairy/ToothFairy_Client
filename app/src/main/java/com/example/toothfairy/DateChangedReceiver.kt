package com.example.toothfairy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.model.repository.WearingInfoRepository
import com.example.toothfairy.view.activity.MainActivity
import com.example.toothfairy.viewModel.MainViewModel

/** 날짜 변경을 감지하는 리시버 클래스 */
class DateChangedReceiver : BroadcastReceiver() {
    private lateinit var mainViewModel:MainViewModel

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        com.example.toothfairy.util.NotifyManager.createNotificationChannel(context)
        com.example.toothfairy.util.NotifyManager.sendNotification(context, "착용 시간 알림", "착용 시간")

        WearingInfoRepository.saveDailyWearingTime()
    }
}
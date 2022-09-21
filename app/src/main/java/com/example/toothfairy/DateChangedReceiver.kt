package com.example.toothfairy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.model.repository.WearingInfoRepository
import com.example.toothfairy.util.MyApplication
import com.example.toothfairy.util.NotifyManager
import com.example.toothfairy.view.activity.MainActivity
import com.example.toothfairy.viewModel.MainViewModel
import java.lang.Exception

/** 날짜 변경을 감지하는 리시버 클래스 */
class DateChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        try{
            WearingInfoRepository.saveDailyWearingTime()
        }catch (e:Exception){
            Log.e("에러", e.printStackTrace().toString())

            NotifyManager.sendNotification(
                context,
                NotifyManager.WEAR_RECOMMEND_NOTIFY_ID,
                "에러 내용",
                e.message.toString()
            )
        }

    }
}
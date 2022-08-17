package com.example.toothfairy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DateChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        when (intent.action) {
            Intent.ACTION_TIME_CHANGED -> Toast.makeText(context, "날짜 바뀜", Toast.LENGTH_SHORT).show()
            Intent.ACTION_TIME_TICK -> Toast.makeText(context, "1분 지남", Toast.LENGTH_SHORT).show()
        }
    }
}
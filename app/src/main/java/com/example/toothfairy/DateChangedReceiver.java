package com.example.toothfairy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.toothfairy.viewModel.MainViewModel;

public class DateChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        switch (intent.getAction()){
            case Intent.ACTION_TIME_CHANGED:
                Toast.makeText(context, "날짜 바뀜", Toast.LENGTH_SHORT).show();

                // 당일 착용 시간 갱신
                
                // 당일 착용 시간으로 WearingStats 갱신
                /* MainViewModel의 updatePatientStats() 호출 */

                // DailyWearingTime 테이블 갱신
                break;
            case Intent.ACTION_TIME_TICK:
                Toast.makeText(context, "1분 지남", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
package com.example.toothfairy;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.toothfairy.data.WearingStats;
import com.example.toothfairy.fragment.HomeFragment;
import com.example.toothfairy.fragment.ProfileFragment;
import com.example.toothfairy.fragment.StatsFragment;
import com.example.toothfairy.viewModel.MainViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // VARIABLE
    MeowBottomNavigation bottomNavigation;
    MainViewModel mainViewModel;

    // Channel에 대한 id 생성 : Channel을 구분하기 위한 ID
    private static final String CHANNEL_ID = "ToothFairy";

    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;

    private DateChangedReceiver dateChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INIT 초기화 작업
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        createNotificationChannel(); // 알림 채널 생성
        initBottomNavibar(); // 네비바 세팅

        dateChangedReceiver = new DateChangedReceiver();
        
        // INIT End

        // 로그인한 유저의 정보를 가져옴
        mainViewModel.loadPatient(getIntent().getStringExtra("loginUser"));

        // 환자 정보가 로드되면 해당 환자의 나이에 맞는 완치자 정보를 로드
        mainViewModel.getPatient().observe(this, (patient)->{
            // 완치 환자 정보 로드
            mainViewModel.loadCuredInfo(patient.getAge());

            // 착용 시간 데이터 로드
            mainViewModel.loadWearingStats();
        });


//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                // 반복실행할 구문
//                createNotification("착용 시간 부족 알림", "착용 시간 부족");
//            }
//        };
//
//        timer.schedule(task, 0, 1000); //Timer 실행

        // timer.cancel();//타이머 종료

        // DailyWearingTime SharedPreference에 저장하고 MutableLiveData로 넣어주기

        
    } // onCreate

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_TIME_CHANGED);

        registerReceiver(dateChangedReceiver, filter);
    }

    private void createNotification(String title, String content){
        // Notivication에 대한 ID 생성
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, "ToothFairy")
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(R.drawable.appnamelogo);

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        //notification manager 생성
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if(android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O){
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"ToothFairy Notification",mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from ToothFairy");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


    // Fragment 변경
    private void loadFragment(Fragment fragment) {
        // 이전 버전까지 호환 가능하도록 getSupportFragmentManager() 사용
        getSupportFragmentManager()
                // 프래그먼트 변경을 위한 트랜잭션을 시작
                .beginTransaction()
                // FrameLayout에 전달 받은 프래그먼트로 교체
                .replace(R.id.frameLayout, fragment)
                // 변경 사항 적용
                .commit();
    }

    private void initBottomNavibar(){
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_chart_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_account_circle_24));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                // 상태 체크
                // 팩토리 패턴 써야할 듯
                switch (item.getId()){
                    case 1:
                        // Home Fragment 초기화
                        fragment = new HomeFragment();
                        break;
                    case 2:
                        // Stats Fragment 초기화
                        fragment = new StatsFragment();
                        break;
                    case 3:
                        // Profile Fragment 초기화
                        fragment = new ProfileFragment();
                        break;
                }
                loadFragment(fragment);
            }
        });

        // 홈 화면을 기본으로 설정
        bottomNavigation.show(1, true);

        // 메뉴 선택시 알림 이벤트
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(), "You Clicked" + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(), "You Reselected " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }
} // StatsActivity
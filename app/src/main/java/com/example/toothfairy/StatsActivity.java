package com.example.toothfairy;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class StatsActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_stats);

        initBottomNavibar();
    } // onCreate

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
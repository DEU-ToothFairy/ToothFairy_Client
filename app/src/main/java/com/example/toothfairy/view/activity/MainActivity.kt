package com.example.toothfairy.view.activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.DateChangedReceiver
import com.example.toothfairy.R
import com.example.toothfairy.databinding.ActivityMainBinding
import com.example.toothfairy.entity.Patient
import com.example.toothfairy.view.fragment.HomeFragment
import com.example.toothfairy.view.fragment.ProfileFragment
import com.example.toothfairy.view.fragment.ReportFragment
import com.example.toothfairy.view.fragment.StatsFragment
import com.example.toothfairy.viewModel.BluetoothViewModel
import com.example.toothfairy.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    // VARIABLE
    private lateinit var binding : ActivityMainBinding
    private lateinit var mainVM  : MainViewModel
    private lateinit var blueVM  : BluetoothViewModel

    // Notification Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private lateinit var mNotificationManager    : NotificationManager
    private lateinit var dateChangedReceiver     : DateChangedReceiver

    private var homeFragment:HomeFragment? = null
    private var statsFragment:StatsFragment? = null
    private var reportFragment:ReportFragment? = null
    private var profileFragment:ProfileFragment? = null
    private var fragmentMap: HashMap<Int, Fragment?>? = hashMapOf(
            R.id.menu_home to homeFragment,
            R.id.menu_stastics to statsFragment,
            R.id.menu_report to reportFragment,
            R.id.menu_profile to profileFragment
        )

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* INIT 초기화 작업 */

        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        mainVM = ViewModelProvider(this)[MainViewModel::class.java]
        blueVM = BluetoothViewModel //ViewModelProvider(this)[BluetoothViewModel::class.java]

        createNotificationChannel() // 알림 채널 생성
        initBottomNavibar() // 네비바 세팅
        loadData()
        settingAppTitle()

        dateChangedReceiver = DateChangedReceiver()
        /* INIT End */

    } // onCreate

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter()

        filter.addAction(Intent.ACTION_TIME_CHANGED)
        registerReceiver(dateChangedReceiver, filter)
    }

    private fun loadData(){

        // 로그인한 유저의 정보를 가져옴
        mainVM!!.loadPatient(intent.getStringExtra("loginUser"))

        // 환자 정보가 로드되면 해당 환자의 나이에 맞는 완치자 정보를 로드
        mainVM!!.patient.observe(this, Observer { patient: Patient? ->
            // 같은 나이의 완치 환자 정보 로드
            patient?.let { mainVM!!.loadCuredInfo(it.age) }

            // 착용 시간 데이터 로드
            mainVM!!.loadWearingStats()
        })

    }

    private fun settingAppTitle(){
        val textData = binding.appTitle.text

        // 3. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(textData)

        // 4-3 index=4에 해당하는 문자열(4)에 빨간색 적용
        val colorBlueSpan = ForegroundColorSpan(Color.BLACK)
        builder.setSpan(colorBlueSpan, 5, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 5. TextView에 적용
        binding.appTitle.text = builder
    }

    /** 알림 생성 */
    private fun createNotification(title: String, content: String) {
        // Notivication에 대한 ID 생성
        val notifyBuilder = NotificationCompat.Builder(this, "ToothFairy")
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setSmallIcon(R.drawable.appnamelogo)

        mNotificationManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    /** 알림 채널 생성 */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        //notification manager 생성
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel 정의 생성자( construct 이용 )
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "ToothFairy Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from ToothFairy"

            // Manager을 이용하여 Channel 생성
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    private fun initBottomNavibar() {
        binding.bottomNavigation.run {
            setOnNavigationItemSelectedListener {
                var fragment = when (it.itemId) {
                    R.id.menu_home -> changetFragment(R.id.menu_home)
                    R.id.menu_stastics -> changetFragment(R.id.menu_stastics)
                    R.id.menu_report -> changetFragment(R.id.menu_report)
                    R.id.menu_notice -> changetFragment(R.id.menu_notice)
                    R.id.menu_profile -> changetFragment(R.id.menu_profile)
                    else -> changetFragment(R.id.menu_home)
                }

                // OnNavigationItemSelectedListner의 반환 값 (람다 함수 형식이라 마지막 라인이 반환 값이 됨)
                true
            }
            selectedItemId = R.id.menu_home
        }
    }

    private fun changetFragment(fragmentId: Int){
        fragmentMap?.forEach { (key, value) ->
            if(key == fragmentId){
                if(fragmentMap!![key] == null){
                    fragmentMap!![key] = fragmentFactory(fragmentId)
                    addFragment(fragmentMap!![key])
                }
                showFragment(fragmentMap!![key])
            }
            else hideFragment(fragmentMap!![key])
        }
    }

    private fun fragmentFactory(fragmentId: Int): Fragment {
        return when (fragmentId) {
            R.id.menu_home -> HomeFragment()
            R.id.menu_stastics -> StatsFragment()
            R.id.menu_report -> ReportFragment()
            R.id.menu_notice -> ProfileFragment()
            R.id.menu_profile -> ProfileFragment()
            else -> HomeFragment()
        }
    }

    // Fragment 변경
    private fun<T: Fragment> addFragment(fragment: T?) {
        // 이전 버전까지 호환 가능하도록 getSupportFragmentManager() 사용
        fragment?.let {
            supportFragmentManager
                .beginTransaction() // 프래그먼트 변경을 위한 트랜잭션을 시작
                .add(R.id.frameLayout, it) // FrameLayout에 전달 받은 프래그먼트로 교체
                .commit() // 변경 사항 적용
        }
    }

    private fun<T: Fragment> showFragment(fragment: T?){
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .show(it)
                .commit()
        }
    }

    private fun<T: Fragment> hideFragment(fragment: T?){
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .hide(it)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        // Channel에 대한 id 생성 : Channel을 구분하기 위한 ID
        private const val CHANNEL_ID = "ToothFairy"
        private const val NOTIFICATION_ID = 0
    }
} // StatsActivity

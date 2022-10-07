package com.example.toothfairy.view.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.toothfairy.R
import com.example.toothfairy.application.MyApplication
import com.example.toothfairy.databinding.ActivityLoginBinding
import com.example.toothfairy.util.Event
import com.example.toothfairy.viewModel.LoginViewModel
import java.util.*

class LoginActivity : AppCompatActivity() {
    
    // VARIABLE : 애니메이션 변수
    var scaleAnim: Animation? = null
    var mainFieldAnim: Animation? = null
    
    // VARIABLE : 데이터 바인딩 변수
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setAnimation()
        login()
    }

    private fun login() {
        val sharedPreferences:SharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE)

        // 자동 로그인
        loginViewModel.autoLogin(sharedPreferences)

        // 로그인 에러 이벤트
        loginViewModel.error.observe(this, Observer { error: Event<String>? ->
            val errorMessage = error?.contentIfNotHandled()

            // 로그인 실패 에러 메시지 출력
            if (!Objects.isNull(errorMessage)) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        })

        // 로그인 버튼 이벤트 리스너 등록
        binding!!.loginBtn.setOnClickListener {
            val id = binding!!.userId.editText!!.text.toString()
            val password = binding!!.password.editText!!.text.toString()

            if (id.isNullOrEmpty()) {
                binding!!.userId.error = "빈 칸 없이 기재해주세요"
                return@setOnClickListener
            }
            if (password.isNullOrEmpty()) {
                binding!!.password.error = "빈 칸 없이 기재해주세요"
                return@setOnClickListener
            }

            // AutoLogin 체크 시 로그인 정보 저장
            if (binding!!.autoLoginCheckBox.isChecked) loginViewModel.saveLoginData(sharedPreferences, id, password )

            loginViewModel.login(id, password)
        }

        // 로그인 성공 시
        loginViewModel.loginFlag.observe(this, Observer {
            // 블루투스 화면으로 전환
//            val bluetoothIntent = Intent(applicationContext, BluetoothActivity::class.java)
//            bluetoothIntent.putExtra("loginUser", value)

            val mainIntent = Intent(applicationContext, MainActivity::class.java)

            startActivity(mainIntent)
            this.finish()
        })
    }

    private fun setAnimation() {
        // 애니메이션 로드
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.mainlogo_animation)
        mainFieldAnim = AnimationUtils.loadAnimation(this, R.anim.mainfield_animation)

        // 애니메이션을 세팅
        binding!!.userId.animation = mainFieldAnim
        binding!!.password.animation = mainFieldAnim
        binding!!.loginBtn.animation = mainFieldAnim
        binding!!.signUpBtn.animation = mainFieldAnim
        binding!!.autoLoginCheckBox.animation = mainFieldAnim

    }
}
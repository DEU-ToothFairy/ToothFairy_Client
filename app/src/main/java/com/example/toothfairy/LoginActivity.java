package com.example.toothfairy;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.example.toothfairy.databinding.ActivityLoginBinding;
import com.example.toothfairy.viewModel.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Animation scaleAnim, mainFieldAnim;
    ActivityLoginBinding binding;
    LoginViewModel loginViewModel = LoginViewModel.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH
                    },
                    1);
        }

        setAnimation();

        login();
    }

    private void login(){
        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", LoginActivity.MODE_PRIVATE);

        // ?????? ?????????
        loginViewModel.autoLogin(sharedPreferences);

        // ????????? ?????? ?????????
        loginViewModel.getError().observe(this, error -> {
            String errorMessage = error.getContentIfNotHandled();
            
            // ????????? ?????? ?????? ????????? ??????
            if(!Objects.isNull(errorMessage)) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // ????????? ?????? ????????? ????????? ??????
        binding.loginBtn.setOnClickListener(view -> {
            String id = binding.userId.getEditText().getText().toString();
            String password = binding.password.getEditText().getText().toString();

            if("".equals(id)) {
                binding.userId.setError("??? ??? ?????? ??????????????????");
                return;
            }
            if("".equals(password)){
                binding.password.setError("??? ??? ?????? ??????????????????");
                return;
            }

            // AutoLogin ?????? ??? ????????? ?????? ??????
            if(binding.autoLoginCheckBox.isChecked()) loginViewModel.saveLoginData(sharedPreferences, id, password);

            loginViewModel.login(id, password);
        });

        // ????????? ?????? ???
        loginViewModel.getLoginUser().observe(this, (value)->{
            // ???????????? ???????????? ??????
            Intent bluetoothIntent = new Intent(getApplicationContext(), BluetoothActivity.class);
            bluetoothIntent.putExtra("loginUser", value);
            startActivity(bluetoothIntent);

            this.finish();
//            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//            mainIntent.putExtra("loginUser", value);
//
//            startActivity(mainIntent);
        });
    }

    private void setAnimation(){
        // ??????????????? ??????
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.mainlogo_animation);
        mainFieldAnim = AnimationUtils.loadAnimation(this, R.anim.mainfield_animation);

        // ?????????????????? ??????
        binding.mainLogo.setAnimation(scaleAnim);
        binding.userId.setAnimation(mainFieldAnim);
        binding.password.setAnimation(mainFieldAnim);
        binding.loginBtn.setAnimation(mainFieldAnim);
        binding.signUpBtn.setAnimation(mainFieldAnim);
        binding.autoLoginCheckBox.setAnimation(mainFieldAnim);
//        image.setAnimation(scaleAnim);
//        usernameInput.setAnimation(mainFieldAnim);
//        passwordInput.setAnimation(mainFieldAnim);
//        loginBtn.setAnimation(mainFieldAnim);
//        singUpBtn.setAnimation(mainFieldAnim);
    }
}

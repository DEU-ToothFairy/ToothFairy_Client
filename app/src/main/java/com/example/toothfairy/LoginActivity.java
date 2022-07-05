package com.example.toothfairy;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    Animation scaleAnim, mainFieldAnim;
    ImageView image;
    TextInputLayout usernameInput, passwordInput;
    Button loginBtn, singUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowMana ger.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initVariable();

        setAnimation();

        loginBtn.setOnClickListener(view -> {
            Intent mainIntent = new Intent(getApplicationContext(), StatsActivity.class);
            startActivity(mainIntent);
        });
    }

    private void initVariable(){
        image = findViewById(R.id.mainLogo);
        usernameInput = (TextInputLayout) findViewById(R.id.username);
        passwordInput = (TextInputLayout) findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        singUpBtn = findViewById(R.id.signUpBtn);
    }

    private void setAnimation(){
        // 애니메이션 로드
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.mainlogo_animation);
        mainFieldAnim = AnimationUtils.loadAnimation(this, R.anim.mainfield_animation);

        // 애니메이션을 세팅
        image.setAnimation(scaleAnim);
        usernameInput.setAnimation(mainFieldAnim);
        passwordInput.setAnimation(mainFieldAnim);
        loginBtn.setAnimation(mainFieldAnim);
        singUpBtn.setAnimation(mainFieldAnim);
    }
}

package com.example.toothfairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.toothfairy.databinding.ActivityBluetoothBinding;

public class BluetoothActivity extends AppCompatActivity {

    ActivityBluetoothBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth);

        binding.bluetoothPulse.startRippleAnimation();

//        Glide.with(this).load(R.raw.bluetooth_character).into(binding.centerImage);

        // 펄스 애니메이션 멈추기
        // binding.bluetoothPulse.stopRippleAnimation();
    }
}
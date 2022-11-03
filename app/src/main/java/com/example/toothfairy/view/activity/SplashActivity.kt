package com.example.toothfairy.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.toothfairy.R
import com.example.toothfairy.application.MyApplication
import com.example.toothfairy.model.repository.WearingInfoRepository


class SplashActivity : AppCompatActivity() {
    // 필요한 권한 목록
    private val permissionList:Array<String> = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(!checkPermission()) requestPermission()

        splashLogo(3L)
    }

    private fun splashLogo(sec: Long){
        Handler().postDelayed(Runnable {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent) //intent 에 명시된 액티비티로 이동
            finish() //현재 액티비티 종료
        }, 1000L * sec)
    }

    /** 권한을 체크하는 메소드 */
    private fun checkPermission():Boolean{
        for (permission in permissionList){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    /** 퍼미션 리스트를 받아서 권한을 요청하는 메소드 */
    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(permissionList,1)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH),1)
        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        var checkResult = true
//
//        if(requestCode == 1){
//            for(i in grantResults){
//                if(i != PackageManager.PERMISSION_GRANTED){
//                    checkResult = false
//                    break
//                }
//            }
//        }
//
//        if(!checkResult) finish()
//    }
}
package com.example.toothfairy.util;

import android.app.Application;
import android.content.Context;

// Context를 전역적으로 받기 위한 클래스
public class MyApplication extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context ApplicationContext(){
        return MyApplication.context;
    }
}

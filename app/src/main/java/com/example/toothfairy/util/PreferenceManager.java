package com.example.toothfairy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferenceManager {
    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = 0;
    private static final long DEFAULT_VALUE_LONG = 0L;
    private static final float DEFAULT_VALUE_FLOAT = 0F;
    private SharedPreferences prefs;

    public PreferenceManager(String dbName) {
        Context context = MyApplication.ApplicationContext();
        prefs = context.getSharedPreferences(dbName, context.MODE_PRIVATE);
    }

    /**
     * String 값 저장
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);
        editor.commit();
    }
    /**
     * boolean 값 저장
     * @param key
     * @param value
     */
    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }
    /**

     * int 값 저장
     * @param key
     * @param value
     */
    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * long 값 저장
     * @param key
     * @param value
     */
    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * float 값 저장
     * @param key
     * @param value
     */
    public void setFloat(String key, float value) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat(key, value);
        editor.commit();
    }
    /**
     * String 값 로드
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);

        return value;
    }

    /**
     * boolean 값 로드
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
    }

    /**
     * int 값 로드
     * @param key
     * @return
     */
    public int getInt(String key) {
        return prefs.getInt(key, DEFAULT_VALUE_INT);
    }

    /**
     * long 값 로드
     * @param key
     * @return
     */
    public long getLong(String key) {
        return prefs.getLong(key, DEFAULT_VALUE_LONG);
    }


    /**
     * float 값 로드
     * @param key
     * @return
     */
    public float getFloat(String key) {
        return prefs.getFloat(key, DEFAULT_VALUE_FLOAT);
    }

    /**
     * 키 값 삭제
     * @param key
     */
    public void removeKey(String key) {
        SharedPreferences.Editor edit = prefs.edit();

        edit.remove(key);
        edit.commit();
    }

    /**
     * 모든 저장 데이터 삭제
     */

    public void clear() {
        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();
        edit.commit();
    }
}
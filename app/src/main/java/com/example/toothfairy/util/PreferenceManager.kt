package com.example.toothfairy.util

import android.content.Context
import android.content.SharedPreferences
import com.example.toothfairy.application.MyApplication

class PreferenceManager(dbName: String?) {
    private val prefs: SharedPreferences

    /**
     * String 값 저장
     * @param key
     * @param value
     */
    fun setString(key: String?, value: String?) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * boolean 값 저장
     * @param key
     * @param value
     */
    fun setBoolean(key: String?, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     *
     * int 값 저장
     * @param key
     * @param value
     */
    fun setInt(key: String?, value: Int) {
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * long 값 저장
     * @param key
     * @param value
     */
    fun setLong(key: String?, value: Long) {
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     * float 값 저장
     * @param key
     * @param value
     */
    fun setFloat(key: String?, value: Float) {
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    /**
     * String 값 로드
     * @param key
     * @return
     */
    fun getString(key: String?): String? {
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }

    /**
     * boolean 값 로드
     * @param key
     * @return
     */
    fun getBoolean(key: String?): Boolean {
        return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
    }

    /**
     * int 값 로드
     * @param key
     * @return
     */
    fun getInt(key: String?): Int {
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    /**
     * long 값 로드
     * @param key
     * @return
     */
    fun getLong(key: String?): Long {
        return prefs.getLong(key, DEFAULT_VALUE_LONG)
    }

    /**
     * float 값 로드
     * @param key
     * @return
     */
    fun getFloat(key: String?): Float {
        return prefs.getFloat(key, DEFAULT_VALUE_FLOAT)
    }

    /**
     * 키 값 삭제
     * @param key
     */
    fun removeKey(key: String?) {
        val edit = prefs.edit()
        edit.remove(key)
        edit.commit()
    }

    /**
     * 모든 저장 데이터 삭제
     */
    fun clear() {
        val edit = prefs.edit()
        edit.clear()
        edit.commit()
    }

    companion object {
        private const val DEFAULT_VALUE_STRING = ""
        private const val DEFAULT_VALUE_BOOLEAN = false
        private const val DEFAULT_VALUE_INT = 0
        private const val DEFAULT_VALUE_LONG = 0L
        private const val DEFAULT_VALUE_FLOAT = 0f
    }

    init {
        val context = MyApplication.ApplicationContext()
        prefs = context!!.getSharedPreferences(dbName, Context.MODE_PRIVATE)
    }
}
package com.example.toothfairy.util

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class ApiLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "Retrofit2 Logger"
        if(message.startsWith("{") || message.startsWith("[")){
            try{
                val prettyPrintJson = GsonBuilder().setPrettyPrinting().create().toJson(JsonParser().parse(message))

                Log.d(logName, prettyPrintJson)
            }catch (e: JsonSyntaxException){
                Log.d(logName, message)
            }
        }
        else Log.d(logName, message)
    }
}
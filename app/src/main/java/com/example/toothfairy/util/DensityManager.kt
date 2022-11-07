package com.example.toothfairy.util

import com.example.toothfairy.application.MyApplication

object DensityManager {
    fun convertDPtoPX(dp:Int):Int {
        return MyApplication.ApplicationContext()?.let {
            (dp * it.resources.displayMetrics.density).toInt()
        }!!
    }
}
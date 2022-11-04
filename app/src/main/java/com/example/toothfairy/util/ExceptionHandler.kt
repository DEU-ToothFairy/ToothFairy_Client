package com.example.toothfairy.util

import kotlin.system.exitProcess

class ExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(p0: Thread, p1: Throwable) {
        p1.printStackTrace()
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }
}
package com.example.toothfairy.util

import android.os.Environment
import java.io.File

val rootFolder =
    File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
        "ToothFairy${File.separator}"
    ).apply {
        if (!exists())
            mkdirs()
    }

fun makeTempFile(): File = File.createTempFile("${System.currentTimeMillis()}", ".png", rootFolder)
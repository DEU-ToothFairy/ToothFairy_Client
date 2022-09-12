package com.example.toothfairy.viewModel

import androidx.lifecycle.ViewModel
import com.example.toothfairy.model.repository.WearingInfoRepository
import androidx.lifecycle.MutableLiveData
import android.content.Context
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

object BluetoothViewModel : ViewModel() {
    var bluetoothData = MutableLiveData<String>()
    var wearStatus = MutableLiveData<Boolean>()
    var connected = MutableLiveData<Boolean>()
    var wearingFlag = MutableLiveData(false)

    fun changeFlag() { wearingFlag.value = !wearingFlag.value!! }

    fun getTime(): String =
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(System.currentTimeMillis()))
}
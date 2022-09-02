package com.example.toothfairy.viewModel

import androidx.lifecycle.ViewModel
import com.example.toothfairy.model.repository.WearingInfoRepository
import androidx.lifecycle.MutableLiveData
import android.content.Context
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class BluetoothViewModel private constructor() : ViewModel() {
    var bluetoothData = MutableLiveData<String>()
    var wearStatus = MutableLiveData<Boolean>()
    var connected = MutableLiveData<Boolean>()
    var wearingFlag = MutableLiveData(false)

    fun init(patientId: String?) {
        WearingInfoRepository.init(patientId)
    }

    val context: Context? = null

    fun changeFlag() { wearingFlag.value = !wearingFlag.value!! }

    fun getTime(): String = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(System.currentTimeMillis()))


    companion object {
        var instance = BluetoothViewModel()

        // BLE.setLocalName("Demo Gyroscope") of Arduino Nano 33 IoT source Code
        private const val TEST_BLE_DEVICE_NAME = "Tooth Fairy"
        private const val REQUEST_ENABLE_BT = 1
        private const val PERMISSION_REQUEST_COARSE_LOCATION = 1

        //Serivce UUID와 같으면 안됨
        private val UUID_Service = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
        private val UUID_VALUE_READ = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")
    }
}
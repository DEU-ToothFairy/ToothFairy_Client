package com.example.toothfairy

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.toothfairy.databinding.ActivityBluetoothBinding
import com.example.toothfairy.viewmodel.BluetoothViewModel
import com.example.toothfairy.viewmodel.BluetoothViewModel.Companion.instance
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class BluetoothActivity : AppCompatActivity() {
    // 데이터 바인딩 변수
    var binding: ActivityBluetoothBinding? = null
    var viewModel: BluetoothViewModel? = null

    /*------------------------------------------------------------------------------------------------*/
    var btManager: BluetoothManager? = null
    var btAdapter: BluetoothAdapter? = null
    var btScanner: BluetoothLeScanner? = null
    var btGatt: BluetoothGatt? = null
    var ValueCharacteristic_read: BluetoothGattCharacteristic? = null

    var strAddress: String? = null
    var strDevicename: String? = null

    var bScanON = false
    var timeCount = 0
    var onCount = 0

    /*------------------------------------------------------------------------------------------------*/
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // INIT
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth)
        binding?.bluetoothPulse?.startRippleAnimation()

        viewModel = instance
        viewModel!!.init(intent.getStringExtra("loginUser"))

        Log.i("BluetoothInstance", viewModel.toString())

        // 검색 버튼 초기화 (숨기기)
        binding?.scanBtn?.visibility = View.INVISIBLE
        binding?.scanBtn?.isEnabled = false

        binding?.initText?.visibility = View.INVISIBLE

        /*------------------------------------------------------------------------------------------------*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH
                ),
                1
            )
        }

        btManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager!!.adapter

        if (btAdapter == null || !btAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }

        btScanner = btAdapter?.bluetoothLeScanner

        startScanning()
        /*------------------------------------------------------------------------------------------------*/
        viewModel!!.connected.observe(this ) { connected: Boolean ->
            // 연결이 되면 장치 초기화 중 텍스트 출력
            if(connected) binding?.initText?.visibility = View.VISIBLE
        }

        // 펄스 애니메이션 멈추기
        // binding.bluetoothPulse.stopRippleAnimation();
    } // onCreate

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (btAdapter == null || !btAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
    }

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            strDevicename = if (result.scanRecord!!.deviceName == null) {
                "Unknown"
            } else {
                result.scanRecord!!.deviceName
            }
            if (bScanON) {
                if (strDevicename == TEST_BLE_DEVICE_NAME) {
                    strAddress = result.device.address
                    stopScanning()
                    if (btAdapter!!.isEnabled) {
                        Toast.makeText(applicationContext, "Stop Scan", Toast.LENGTH_SHORT).show()
                    } else {
                        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
                    }
                    connection()
                } else {
//                    tvStatus.setText("Device not found");
                }
            }
            Log.d("LOG ", "$strAddress  : $strDevicename")
        }
    }
    private val connectecallabck: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            btGatt = gatt
            //gatt.discoverServices();
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices() // onServicesDiscovered() 호출 (서비스 연결 위해 꼭 필요)
                Log.i("Data", "PASS:1")
                viewModel!!.connected.postValue(true)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Can't see.
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i("Data", "PASS:2")
            super.onServicesDiscovered(gatt, status)
            val service = gatt.getService(UUID_Service)
            ValueCharacteristic_read = service.getCharacteristic(UUID_VALUE_READ)

            runOnUiThread { //최초 실행
                btGatt!!.readCharacteristic(ValueCharacteristic_read)
                Log.i("Data", "PASS:3")
            }

            // 액티비티 전환
            changeActivity()
        }



        var tmp = ""
        var strTime = ""
        @SuppressLint("MissingPermission")
        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            val strData = characteristic.getStringValue(0)

            if (tmp != strData) {
                tmp = strData
                strTime = time
            }

            runOnUiThread {
                Log.i("Data", strData + "")
                if (timeCount >= 60) {
                    timeCount = 0
                    if (onCount >= 40) {
                        onCount = 0

                        // 시간 저장 플래그
                        viewModel!!.changeFlag()
                    }
                }

                timeCount++
                if ("ON" == strData) onCount++

                Log.i("Count", "Time : $timeCount ON : $onCount")

                viewModel!!.bluetoothData.postValue(strData)
                btGatt!!.readCharacteristic(ValueCharacteristic_read)
            }
            super.onCharacteristicRead(gatt, characteristic, status)
        }
    }

    @SuppressLint("MissingPermission")
    fun startScanning() {
        btScanner!!.startScan(leScanCallback)
        bScanON = true
    }

    @SuppressLint("MissingPermission")
    fun stopScanning() {
        btScanner!!.stopScan(leScanCallback)
        bScanON = false
    }

    @SuppressLint("MissingPermission")
    fun connection() {
        if (strDevicename != null) {
            if (strDevicename == TEST_BLE_DEVICE_NAME) {
                val device = btAdapter!!.getRemoteDevice(strAddress)
                device.connectGatt(applicationContext, false, connectecallabck)
            } else {
                Toast.makeText(applicationContext, "device not found", Toast.LENGTH_SHORT).show()
            }
        } else {
        }
    }

    @SuppressLint("MissingPermission")
    fun deconnection() {
        if (strDevicename == TEST_BLE_DEVICE_NAME && btGatt != null) {
            btGatt!!.disconnect()
            btGatt!!.close()
            if (btAdapter!!.isEnabled) {
                Toast.makeText(applicationContext, "Disconnect the BLE Device.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            }
        } else {
//            tvStatus.setText("BLE Device not found");
        }
    }

    fun changeActivity(){
        val mainIntent = Intent(applicationContext, MainActivity::class.java)

        mainIntent.putExtra("loginUser", intent.getStringExtra("loginUser"))
        startActivity(mainIntent)
        finish()
    }

    val time: String
        get() {
            val now = System.currentTimeMillis()
            val date = Date(now)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

            return dateFormat.format(date)
        }

    companion object {
        // BLE.setLocalName("Demo Gyroscope") of Arduino Nano 33 IoT source Code
        private const val TEST_BLE_DEVICE_NAME = "Tooth Fairy"
        private const val REQUEST_ENABLE_BT = 1
        private const val PERMISSION_REQUEST_COARSE_LOCATION = 1

        //Serivce UUID와 같으면 안됨
        private val UUID_Service = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
        private val UUID_VALUE_READ = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")
    }
}
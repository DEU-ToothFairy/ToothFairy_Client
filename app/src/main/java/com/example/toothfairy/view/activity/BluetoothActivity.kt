package com.example.toothfairy.view.activity

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.toothfairy.R
import com.example.toothfairy.databinding.ActivityBluetoothBinding
import com.example.toothfairy.util.ExceptionHandler
import com.example.toothfairy.viewModel.BluetoothViewModel
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
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())

        // INIT
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth)
        binding?.bluetoothPulse?.startRippleAnimation()

        viewModel = BluetoothViewModel

        // 검색 버튼 초기화 (숨기기)
        binding?.scanBtn?.visibility = View.INVISIBLE
        binding?.scanBtn?.isEnabled = false

        binding?.initText?.visibility = View.INVISIBLE

        /*------------------------------------------------------------------------------------------------*/

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

        findViewById<TextView>(R.id.searchDeviceText).setOnClickListener{
            changeActivity()
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

    // Device scan callback.
    /** 블루투스 장치를 스캔하는 콜백 메소드 */
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            strDevicename = if (result.scanRecord!!.deviceName == null) {
                "Unknown"
            } else {
                result.scanRecord!!.deviceName
            }
            // 스캔 상태일 경우
            if (bScanON) {
                // 기기의 이름을 찾을 때까지 스캔
                if (strDevicename == TEST_BLE_DEVICE_NAME) {
                    strAddress = result.device.address
                    stopScanning()
                    if (btAdapter!!.isEnabled) {
                        Toast.makeText(applicationContext, "장치가 연결 되었습니다.", Toast.LENGTH_SHORT).show()
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


    /** 장치가 연결되면 해당 콜백 메소드가 실행 됨 */
    private val connectecallabck: BluetoothGattCallback = object : BluetoothGattCallback() { // 익명 클래스
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
            super.onServicesDiscovered(gatt, status)

            Log.i("Data", "PASS:2")
            val service = gatt.getService(UUID_Service)
            ValueCharacteristic_read = service.getCharacteristic(UUID_VALUE_READ)

            runOnUiThread { //최초 실행
                btGatt!!.readCharacteristic(ValueCharacteristic_read)
                Log.i("Data", "PASS:3")
            }

            // 액티비티 전환
            changeActivity()
        }



        @SuppressLint("MissingPermission")
        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            val strData = characteristic.getStringValue(0)

            /** 블루투스 모듈의 데이터 전송 속도와 맞춰지는듯 */
            runOnUiThread {
                Log.i("Data", strData)
                if (timeCount >= 60) {
                    timeCount = 0
                    if (onCount >= 40) {
                        onCount = 0

                        // 착용 상태 플래그
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
    fun deconnection() {
        if (strDevicename == TEST_BLE_DEVICE_NAME && btGatt != null) {
            btGatt!!.disconnect()
            btGatt!!.close()
            if (btAdapter!!.isEnabled) {
                viewModel!!.connected.postValue(false) // 연결 해제 표시

                Toast.makeText(applicationContext, "장치 연결이 해제 되었습니다.", Toast.LENGTH_SHORT).show()
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

//        mainIntent.putExtra("loginUser", intent.getStringExtra("loginUser"))
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
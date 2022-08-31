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

//    val activity: Activity? = null
//    var btManager: BluetoothManager? = null
//    var btAdapter: BluetoothAdapter? = null
//    var btScanner: BluetoothLeScanner? = null
//    var btGatt: BluetoothGatt? = null
//    var valueCharacteristic_read: BluetoothGattCharacteristic? = null
//
//    var strAddress: String? = null
//    var strDevicename: String? = null
//
//    var isBScanON = false
//    @SuppressLint("MissingPermission")
//    fun reConnect() {
//        if (btAdapter == null || !btAdapter!!.isEnabled) {
//            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            activity!!.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun checkPermission() {
//        btManager = activity!!.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        btAdapter = btManager!!.adapter
//
//        if (btAdapter == null || !btAdapter!!.isEnabled) {
//            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
//        }
//
//        // 액티비티 실행 시 바로 스캔 시작
//        btScanner = btAdapter?.bluetoothLeScanner
//
//        Log.i("START", "start 0")
//        startScanning()
//    }
//
//    @SuppressLint("MissingPermission")
//    fun startScanning() {
//        Log.i("START", "start 1")
//        btScanner!!.startScan(leScanCallback)
//        isBScanON = true
//    }
//
//    @SuppressLint("MissingPermission")
//    fun stopScanning() {
//        btScanner!!.stopScan(leScanCallback)
//        isBScanON = false
//    }
//
//    @SuppressLint("MissingPermission")
//    fun connection() {
//        if (strDevicename != null) {
//            if (strDevicename == TEST_BLE_DEVICE_NAME) {
////            tvStatus.setText("device Connect");
//                val device = btAdapter!!.getRemoteDevice(strAddress)
//                device.connectGatt(context, false, connectecallabck)
//            } else {
//                Toast.makeText(context, "device not found", Toast.LENGTH_SHORT).show()
//                //            tvStatus.setText("Device not found");
//            }
//        } else {
////            tvStatus.setText("Can't detected the device!!");
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun deconnection() {
//        if (strDevicename == TEST_BLE_DEVICE_NAME && btGatt != null) {
//            btGatt!!.disconnect()
//            btGatt!!.close()
//            if (btAdapter!!.isEnabled) {
//                Toast.makeText(context, "Disconnect the BLE Device.", Toast.LENGTH_SHORT).show()
//            } else {
//                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                activity!!.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
//            }
//        } else {
////            tvStatus.setText("BLE Device not found");
//        }
//    }
//
//    // Device scan callback.
//    val leScanCallback: ScanCallback = object : ScanCallback() {
//        @SuppressLint("MissingPermission")
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            strDevicename = if (result.scanRecord!!.deviceName == null) {
//                "Unknown"
//            } else {
//                result.scanRecord!!.deviceName
//            }
//            if (isBScanON) {
//                if (strDevicename == TEST_BLE_DEVICE_NAME) {
////                    tvStatus.setText("Device Name: " + strDevicename + "\n");
//                    strAddress = result.device.address
//                    stopScanning()
//                    if (btAdapter!!.isEnabled) {
//                        Toast.makeText(context, "[VIEWMODEL] Stop Scan", Toast.LENGTH_SHORT).show()
//                    } else {
//                        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                        activity!!.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
//                    }
//                    connection()
//                } else {
////                    tvStatus.setText("Device not found");
//                }
//            }
//            Log.d("LOG ", "$strAddress  : $strDevicename")
//        }
//    }
//    val connectecallabck: BluetoothGattCallback = object : BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
//            super.onConnectionStateChange(gatt, status, newState)
//            btGatt = gatt
//            //gatt.discoverServices();
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                gatt.discoverServices() // onServicesDiscovered() 호출 (서비스 연결 위해 꼭 필요)
//                Log.i("Data", "PASS:1")
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                // Can't see.
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            Log.i("Data", "PASS:2")
//            super.onServicesDiscovered(gatt, status)
//            val service = gatt.getService(UUID_Service)
//
//            valueCharacteristic_read = service.getCharacteristic(UUID_VALUE_READ)
//
//            activity!!.runOnUiThread {
//                btGatt!!.readCharacteristic(valueCharacteristic_read)
//                Log.i("Data", "PASS:3")
//            }
//        }
//
//        var tmp = ""
//        var strTime = ""
//
//        @SuppressLint("MissingPermission")
//        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
//            super.onCharacteristicRead(gatt, characteristic, status)
//
//            val strData = characteristic.getStringValue(0)
//
//            if (tmp != strData) {
//                tmp = strData
//                strTime = getTime()
//            }
//
//            activity!!.runOnUiThread { btGatt!!.readCharacteristic(valueCharacteristic_read) }
//
//            super.onCharacteristicRead(gatt, characteristic, status)
//        }
//    }

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
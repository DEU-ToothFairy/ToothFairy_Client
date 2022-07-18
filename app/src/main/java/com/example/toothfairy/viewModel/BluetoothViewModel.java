package com.example.toothfairy.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toothfairy.model.repository.WearingInfoRepository;
import com.example.toothfairy.util.MyApplication;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.Executors;

import lombok.Getter;

@Getter
public class BluetoothViewModel extends ViewModel {
    public static BluetoothViewModel bluetoothViewModel = new BluetoothViewModel();
    public static BluetoothViewModel getInstance() { return bluetoothViewModel;}
    private BluetoothViewModel() { }


    private WearingInfoRepository wearingInfoRepository = WearingInfoRepository.getInstance();

    MutableLiveData<String> bluetoothData = new MutableLiveData<>();
    MutableLiveData<Boolean> wearStatus = new MutableLiveData<Boolean>();
    MutableLiveData<Boolean> connected = new MutableLiveData<>();
    MutableLiveData<Boolean> wearingFlag = new MutableLiveData<>(false);

    public void init(String patientId){
        wearingInfoRepository.init(patientId);
    }

    private Activity activity;
    private Context context;

    int c = 0;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter=null;
    BluetoothLeScanner btScanner;
    BluetoothGatt btGatt;
    BluetoothGattCharacteristic ValueCharacteristic_read;

    String strAddress;
    String strDevicename;

    // BLE.setLocalName("Demo Gyroscope") of Arduino Nano 33 IoT source Code
    private static final String TEST_BLE_DEVICE_NAME = "Tooth Fairy";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    //Serivce UUID와 같으면 안됨
    private static final UUID UUID_Service = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    private static final UUID UUID_VALUE_READ = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");

    boolean bScanON = false;

    public void changeFlag(){
        this.wearingFlag.setValue(!wearingFlag.getValue());
    }

    @SuppressLint("MissingPermission")
    public void reConnect(){
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }
    }

    @SuppressLint("MissingPermission")
    public void checkPermission(){
        btManager = (BluetoothManager)activity.getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // 액티비티 실행 시 바로 스캔 시작
        btScanner = btAdapter.getBluetoothLeScanner();
        Log.i("START", "start 0");
        startScanning();
    }

    @SuppressLint("MissingPermission")
    public void startScanning() {
        Log.i("START", "start 1");
        btScanner.startScan(leScanCallback);

        bScanON = true;
    }

    @SuppressLint("MissingPermission")
    public void stopScanning() {
        btScanner.stopScan(leScanCallback);

        bScanON = false;
    }
    @SuppressLint("MissingPermission")
    public void connection()
    {
        if(strDevicename!=null)
        {   if (strDevicename.equals(TEST_BLE_DEVICE_NAME) )
        {
//            tvStatus.setText("device Connect");

            BluetoothDevice device=btAdapter.getRemoteDevice(strAddress);
            device.connectGatt(context,false,connectecallabck);


        } else {
            Toast.makeText(context,"device not found",Toast.LENGTH_SHORT).show();
//            tvStatus.setText("Device not found");
        }
        } else {
//            tvStatus.setText("Can't detected the device!!");
        }
    }

    @SuppressLint("MissingPermission")
    public void deconnection()
    {
        if(strDevicename.equals(TEST_BLE_DEVICE_NAME) && btGatt!=null)
        {
            btGatt.disconnect();
            btGatt.close();

            if(btAdapter.isEnabled()){
                Toast.makeText(context,"Disconnect the BLE Device.", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                activity.startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
            }

        } else {
//            tvStatus.setText("BLE Device not found");
        }
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            if (result.getScanRecord().getDeviceName() == null ) {
                strDevicename = "Unknown";
            } else {
                strDevicename = result.getScanRecord().getDeviceName();
            }


            if (bScanON) {
                if (strDevicename.equals(TEST_BLE_DEVICE_NAME)) {
//                    tvStatus.setText("Device Name: " + strDevicename + "\n");

                    strAddress = result.getDevice().getAddress();
                    stopScanning();

                    if (btAdapter.isEnabled()) {
                        Toast.makeText(context, "Stop Scan", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    }

                    connection();

                } else {
//                    tvStatus.setText("Device not found");
                }
            }
            Log.d("LOG ", strAddress + "  : " + strDevicename);
        }
    };

    private BluetoothGattCallback connectecallabck = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            btGatt=gatt;
            //gatt.discoverServices();
            if (newState == BluetoothProfile.STATE_CONNECTED) {

                gatt.discoverServices(); // onServicesDiscovered() 호출 (서비스 연결 위해 꼭 필요)
                Log.i("Data", "PASS:1");

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Can't see.
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i("Data", "PASS:2");
            super.onServicesDiscovered(gatt, status);

            BluetoothGattService service = gatt.getService(UUID_Service);

            ValueCharacteristic_read = service.getCharacteristic(UUID_VALUE_READ);

            activity.runOnUiThread(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    btGatt.readCharacteristic(ValueCharacteristic_read);
                    Log.i("Data", "PASS:3");
                }
            });

//            Executors.newSingleThreadExecutor().execute(new Runnable() {
//                @SuppressLint("MissingPermission")
//                @Override
//                public void run() {
//                    //최초 실행
//                    btGatt.readCharacteristic(ValueCharacteristic_read);
//                    Log.i("Data", "PASS:3");
//                }
//            });
        }

        public String tmp = "";
        public String strTime = "";

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,final BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);


            String strData = characteristic.getStringValue(0);

            if(!tmp.equals(strData))
            {
                tmp = strData;
                strTime = getTime();
            }

            activity.runOnUiThread(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    btGatt.readCharacteristic(ValueCharacteristic_read);
                }
            });

//            Executors.newSingleThreadExecutor().execute(new Runnable() {
//                @SuppressLint("MissingPermission")
//                @Override
//                public void run() {
//                    btGatt.readCharacteristic(ValueCharacteristic_read);
//                }
//            });

            super.onCharacteristicRead(gatt, characteristic, status);

//            (MainActivity.this).runOnUiThread(new Runnable(){
//
//                @SuppressLint("MissingPermission")
//                @Override
//                public void run() {
////                    tvValue.setText("Read Value : " + strData + String.valueOf(c));
//
////                    tvTime.setText("Read Time : " + strTime);
////                    Log.i("Data", tvValue.getText().toString());
//                    c++;
//
//
//                    btGatt.readCharacteristic(ValueCharacteristic_read);
//                }
//            });
        }
    };

    public String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }

}

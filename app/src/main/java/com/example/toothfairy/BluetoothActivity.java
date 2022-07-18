package com.example.toothfairy;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.toothfairy.databinding.ActivityBluetoothBinding;
import com.example.toothfairy.viewModel.BluetoothViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;
public class BluetoothActivity extends AppCompatActivity {

    ActivityBluetoothBinding binding;
    BluetoothViewModel viewModel;

    /*------------------------------------------------------------------------------------------------*/

    BluetoothManager btManager;
    BluetoothAdapter btAdapter=null;
    BluetoothLeScanner btScanner;
    BluetoothGatt btGatt;
    BluetoothGattCharacteristic ValueCharacteristic_read;

    String strAddress;
    String strDevicename;

    // BLE.setLocalName("Demo Gyroscope") of Arduino Nano 33 IoT source Code
    private final static String TEST_BLE_DEVICE_NAME = "Tooth Fairy";
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    //Serivce UUID와 같으면 안됨
    private static final UUID UUID_Service = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    private static final UUID UUID_VALUE_READ = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");

    boolean bScanON = false;
    int timeCount = 0;
    int onCount = 0;

    /*------------------------------------------------------------------------------------------------*/
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // INIT
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth);
        binding.bluetoothPulse.startRippleAnimation();


        viewModel = BluetoothViewModel.getInstance();
        viewModel.init(getIntent().getStringExtra("loginUser"));

        Log.i("BluetoothInstance", viewModel.toString());

        // 검색 버튼 초기화 (숨기기)
        binding.scanBtn.setVisibility(View.INVISIBLE);
        binding.scanBtn.setEnabled(false);

        binding.initText.setVisibility(View.INVISIBLE);

        /*------------------------------------------------------------------------------------------------*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION

                    },
                    1);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH

                    },
                    1);
        }


        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);

        }

        btScanner = btAdapter.getBluetoothLeScanner();
        startScanning();
        /*------------------------------------------------------------------------------------------------*/

        viewModel.getConnected().observe(this, (connected)->{
            if(connected) binding.initText.setVisibility(View.VISIBLE);
        });

        // 펄스 애니메이션 멈추기
        // binding.bluetoothPulse.stopRippleAnimation();
    } // onCreate

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
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
                    strAddress = result.getDevice().getAddress();
                    stopScanning();

                    if (btAdapter.isEnabled()) {
                        Toast.makeText(getApplicationContext(), "Stop Scan", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
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
                viewModel.getConnected().postValue(true);

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



            (BluetoothActivity.this).runOnUiThread(new Runnable(){
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    //최초 실행
                    btGatt.readCharacteristic(ValueCharacteristic_read);
                    Log.i("Data", "PASS:3");
                }
            });

            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);

            mainIntent.putExtra("loginUser", getIntent().getStringExtra("loginUser"));
            startActivity(mainIntent);

            finish();
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
                strTime=getTime();
            }


            (BluetoothActivity.this).runOnUiThread(new Runnable(){
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    Log.i("Data", strData + "");

                    if(timeCount >= 60){
                        timeCount = 0;
                        if(onCount >= 40){
                            onCount = 0;

                            // 시간 저장 플래그
                            viewModel.changeFlag();
                        }
                    }

                    timeCount++;
                    if("ON".equals(strData)) onCount++;

                    Log.i("Count", "Time : " + timeCount + " ON : " + onCount);

                    viewModel.getBluetoothData().postValue(strData);

                    btGatt.readCharacteristic(ValueCharacteristic_read);
                }
            });

            super.onCharacteristicRead(gatt, characteristic, status);
        }
    };

    @SuppressLint("MissingPermission")
    public void startScanning() {
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

            BluetoothDevice device=btAdapter.getRemoteDevice(strAddress);
            device.connectGatt(getApplicationContext(),false,connectecallabck);

        } else {
            Toast.makeText(getApplicationContext(),"device not found",Toast.LENGTH_SHORT).show();
        }
        } else {

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
                Toast.makeText(getApplicationContext(),"Disconnect the BLE Device.", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
            }

        } else {
//            tvStatus.setText("BLE Device not found");
        }
    }

    public String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }
}
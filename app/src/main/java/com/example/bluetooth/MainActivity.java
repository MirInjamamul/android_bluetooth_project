package com.example.bluetooth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ListView listView;
    private ArrayAdapter arrayAdapter;

    // BLE SCANNER and ADVERTISER
    private BluetoothLeScanner bluetoothLeScanner;

    private boolean scanning;
    private Handler handler = new Handler();

    // Stop Scanning after 30 seconds
    private static final long SCAN_PERIOD = 20000;
    private static final int ADVERTISE_TIMEOUT = 0;

    private static final AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(false)
            .build();

    private static final AdvertiseData advertiseData = new AdvertiseData.Builder()
            .setIncludeDeviceName(true)  // Include device name
            .addServiceUuid(ParcelUuid.fromString("00002a00-0000-1000-8000-00805f9b34fb"))  // Add a custom service UUID
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOn = (Button)findViewById(R.id.turnOnBtn);
        Button btnOff = (Button)findViewById(R.id.turnOffBtn);
        Button btnDiscoverable = (Button)findViewById(R.id.discoverableBtn);
        Button btnGetPairedDevice = (Button)findViewById(R.id.getPairedBtn);

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOn();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAdvertising();
                turnOff();

            }
        });

        btnDiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvertising();
            }
        });

        btnGetPairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getPairedDevice();
                scanLeDevice();
            }
        });
    }

    void turnOn(){
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "BlueTooth not supported", Toast.LENGTH_SHORT).show();
        }else {
            if(!bluetoothAdapter.isEnabled()){
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);
                Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Bluetooth already turned on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void discoverableOn(){
        if(!bluetoothAdapter.isDiscovering()){
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),1);
            Toast.makeText(getApplicationContext(), "Making Device Discoverable", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Already Discoverable started", Toast.LENGTH_SHORT).show();
        }
    }

    public void startAdvertising(){
        if(bluetoothAdapter != null){
            if(bluetoothAdapter.isMultipleAdvertisementSupported()){
                BluetoothLeAdvertiser advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
                advertiser.startAdvertising(settings, advertiseData, advertiseCallback);
            }
        }else{}
    }

    public void stopAdvertising(){
        if(bluetoothAdapter != null){
            BluetoothLeAdvertiser advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
            advertiser.stopAdvertising(advertiseCallback);
        }
    }

    void getPairedDevice(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        ArrayList list = new ArrayList();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                String device_name = device.getName();
                String mac_address = device.getAddress();

                list.add("Name : "+ device_name + "Mac Address : "+mac_address);
            }

            listView = (ListView)findViewById(R.id.pairedListView);

            arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list);

            listView.setAdapter(arrayAdapter);
        }
    }

    private void scanLeDevice(){
        if(bluetoothAdapter != null){
            if(bluetoothAdapter.isEnabled()){
                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
                ScanSettings settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();

                List<ScanFilter> filters = new ArrayList<>();

                bluetoothLeScanner.startScan(leScanCallBack);

                Log.d("SCANNER", "scanLeDevice: started");
            }else{}
        }
    }

    void turnOff(){
        if(!bluetoothAdapter.isEnabled()){
            Toast.makeText(getApplicationContext(), "Bluetooth is already Off", Toast.LENGTH_SHORT).show();
        }else {
            bluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth turned off", Toast.LENGTH_SHORT).show();
        }
    }

    private ScanCallback leScanCallBack = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.d("Result ", result.getDevice().getName());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("SCANNER", "onScanFailed: ");
        }
    };

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d("ADVERTISE", "onStartSuccess: ");
        }

    };
}

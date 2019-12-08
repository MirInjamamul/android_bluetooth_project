package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ListView listView;
    private ArrayAdapter arrayAdapter;

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
                turnOff();
            }
        });

        btnDiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverableOn();
            }
        });

        btnGetPairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPairedDevice();
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

    void turnOff(){
        if(!bluetoothAdapter.isEnabled()){
            Toast.makeText(getApplicationContext(), "Bluetooth is already Off", Toast.LENGTH_SHORT).show();
        }else {
            bluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth turned off", Toast.LENGTH_SHORT).show();
        }
    }
}

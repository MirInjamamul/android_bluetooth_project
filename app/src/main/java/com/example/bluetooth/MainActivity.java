package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOn = (Button)findViewById(R.id.turnOnBtn);
        Button btnOff = (Button)findViewById(R.id.turnOffBtn);
        Button btnDiscoverable = (Button)findViewById(R.id.discoverableBtn);

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

    void turnOff(){
        if(!bluetoothAdapter.isEnabled()){
            Toast.makeText(getApplicationContext(), "Bluetooth is already Off", Toast.LENGTH_SHORT).show();
        }else {
            bluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth turned off", Toast.LENGTH_SHORT).show();
        }
    }
}

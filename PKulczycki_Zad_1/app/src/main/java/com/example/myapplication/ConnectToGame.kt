package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class ConnectToGame : AppCompatActivity() {
    private lateinit var ButtonBack: Button
    private lateinit var ButtonFinBtooth: Button
    private var bToothAdapter: BluetoothAdapter? = null
    private lateinit var Devices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bt_connect)

        ButtonBack = findViewById(R.id.backButton)
        ButtonFinBtooth = findViewById(R.id.Refreash)
        bToothAdapter = BluetoothAdapter.getDefaultAdapter()

        ButtonFinBtooth.setOnClickListener {
            if(bToothAdapter != null){
                if(!bToothAdapter!!.isEnabled){
                    val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBTIntent,REQUEST_ENABLE_BLUETOOTH)
                }
            }
        }

        ButtonBack.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun pairedDeviceList(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
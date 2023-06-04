package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class ConnectToGame : AppCompatActivity() {
    private  lateinit var ListaBT: ListView
    private lateinit var Listadapter: ArrayAdapter<String>
    private lateinit var pairedDevices: ArrayList<BluetoothDevice>
    private lateinit var dataList: ArrayList<String>
    private lateinit var ButtonBack: Button
    private lateinit var ButtonFinBtooth: Button
    private var BAdapter: BluetoothAdapter? = null
    private lateinit var BMenager: BluetoothManager
    private lateinit var Devices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1
    private val reqeustPremisssionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                //Log.i("Permission: ", "Granted")
                //print("is granted")
            } else {
                //Log.i("Permission: ", "Granted")
                //println("is not granted")
                //println(isGranted)
            }

        }

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"
    }

    private val receiver = object : BroadcastReceiver () {
        override fun onReceive(context:Context, intent: Intent) {
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    if (ActivityCompat.checkSelfPermission(
                            this@ConnectToGame,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        if (device != null) {
                            dataList.add(device.name)
                            pairedDevices.add(device)
                        }
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bt_connect)

        pairedDevices = ArrayList()
        ListaBT = findViewById(R.id.BTList)
        dataList = ArrayList()
        Listadapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,dataList)
        ListaBT.adapter = Listadapter
        ButtonBack = findViewById(R.id.backButton)
        ButtonFinBtooth = findViewById(R.id.Refreash)
        BMenager = getSystemService(BluetoothManager::class.java)
        BAdapter = BMenager.adapter
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)


        ButtonFinBtooth.setOnClickListener {
            dataList.clear()
            println(" click refresh")
            if(BAdapter != null){
                if (BAdapter?.isEnabled == false){
                    println(" Włącz Bluetooth !!!")
                } else {
                    println(" Szukam urządzeń !!!")
                    if(reqPremisionCONNECT() == 0){
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            println(" Dodajemy obecnie połączone urządzenia ")
                            Devices = BAdapter?.bondedDevices!!
                            Devices.forEach { device ->
                                run {
                                    dataList.add(device.name )
                                    pairedDevices.add(device)
                                }
                            }
                            run {
                                dataList.add("uwaga jeżeli nie ma na tej liśie urządzenia HC-06 sparuj się z nim w ustawieniach bluetooth")
                            }
                        }
                    }
                }
            } else{
                println(" nie ma dostepu to tej funkcji ")
            }
            Listadapter.notifyDataSetChanged()
        }

        ListaBT.setOnItemClickListener { adapterView, view, i, l ->
            println("? to sie wgl wywołuje?")
            pairedDevices.forEach { dev ->
                run {
                    if (dev.name == dataList[i]) {
                        println("trying to connent to: "+ dev.name)
                        goAndConnect(dev)
                    }
                }
            }
        }

        ButtonBack.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goAndConnect(device: BluetoothDevice){
        MainActivity.DevToConnect.Device = device;
        intent = Intent(this, MainActivity::class.java).also {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val pom = "Connected to " + device.name.toString()
                intent.putExtra("CONN_MESS", pom)
            }
        }
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
    }




    private fun reqPremisionCONNECT(): Int {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> {
                println(" we have premmision Connect")
                return 0

            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) -> {
                //println(" acsing for premission 1")
                // should display sth why u need it

                reqeustPremisssionLauncher.launch(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }
            else -> {
                //println(" acsing for premission 2")
                reqeustPremisssionLauncher.launch(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
                // not asced yet
            }
        }
        println(" something fucked up boss")
        return 1

    }
    private fun reqPremision(): Int {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED -> {
                println(" we have premmision scan")
                return 0

            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) -> {
                //println(" acsing for premission 1")
                // should display sth why u need it

                reqeustPremisssionLauncher.launch(
                    Manifest.permission.BLUETOOTH_SCAN
                )
            }
            else -> {
                //println(" acsing for premission 2")
                reqeustPremisssionLauncher.launch(
                    Manifest.permission.BLUETOOTH_SCAN
                )
                // not asced yet
            }
        }
        println(" something fucked up boss")
        return 1

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
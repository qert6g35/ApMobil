package com.example.myapplication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.IOException
import java.io.OutputStream
import java.sql.Connection
import java.util.*

class MainActivity : AppCompatActivity() {
    public var Connection: ConnectThread? = null
    private lateinit var ButtonForward: Button
    private lateinit var ButtonLeft: Button
    private lateinit var ButtonRight: Button
    private lateinit var ButtonConnect: Button
    private lateinit var AlertText: TextView
    private var isConnented = false
    private var message = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButtonForward = findViewById<Button>(R.id.buttonC)
        ButtonLeft = findViewById<Button>(R.id.buttonL)
        ButtonRight  = findViewById<Button>(R.id.buttonR)
        ButtonConnect = findViewById<Button>(R.id.connect)
        AlertText = findViewById(R.id.infoText)
        AlertText.text = "Loadin App"
        if(DevToConnect.Device != null){
            Connection = ConnectThread(DevToConnect.Device!!)
            AlertText.text = intent.getStringExtra("message")
            AlertText.text = "getting conection"
            Connection!!.run()
        }

        ButtonForward.setOnClickListener {
            message = "↑";
            AlertText.text = message

        }

        ButtonLeft.setOnClickListener {
            message = "←";
            AlertText.text =  message
        }

        ButtonRight.setOnClickListener {
            message = "→";
            AlertText.text  = message
        }
        ButtonConnect.setOnClickListener {
           intent = Intent(this, ConnectToGame::class.java)
            startActivity(intent)
        }

    }

    public object DevToConnect{
        var Device: BluetoothDevice? = null
    }

    @SuppressLint("MissingPermission")
    inner class ConnectThread(device: BluetoothDevice) : Thread() {
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(uuid)
        }
        private val mmOutStream: OutputStream = mmSocket?.outputStream as OutputStream
        public var isConnected: Boolean = false
        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            println("attempting a connection")
            try {
                mmSocket?.connect()
                isConnected = true;
            }catch (e: IOException){
                println(e)
            }

        }

        fun write(message: Int){
            try{
                mmOutStream.write(message)
                println(message)
            } catch (e: IOException) {
                println(e)
            }
        }

        // Closes the client socket and causes the thread to finish.
        public fun cancel() {
            try {
                mmSocket?.close()
                isConnected = false;
            } catch (e: IOException) {
                println(e)
            }
        }
    }


}
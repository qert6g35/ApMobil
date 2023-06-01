package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
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

}
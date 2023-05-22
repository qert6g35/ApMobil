package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var AlertText: TextView
    private var isConnented = false
    private var message = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //MessageBox.text = "Loading App"
        AlertText = findViewById(R.id.infoText)
        AlertText.text = "Loadin App"

        var Button = findViewById<Button>(R.id.buttonC)
        Button.setOnClickListener {
            message = "";
            //AlertText.text = message
        }

        Button = findViewById<Button>(R.id.buttonL)
        Button.setOnClickListener {
            message = "L";
            //AlertText.text =  message
        }

        Button = findViewById<Button>(R.id.buttonR)
        Button.setOnClickListener {
            message = "R";
            //AlertText.text  = message
        }
    }

}
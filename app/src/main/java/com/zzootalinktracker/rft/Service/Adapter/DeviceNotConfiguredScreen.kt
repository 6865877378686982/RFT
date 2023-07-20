package com.zzootalinktracker.rft.Service.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.zzootalinktracker.rft.R

class DeviceNotConfiguredScreen : AppCompatActivity() {

    private lateinit var tvNoDeviceFoundMsg: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devive_not_configured_screen)

        tvNoDeviceFoundMsg = findViewById(R.id.tvNoDeviceFoundMsg)
        val msg = intent.extras!!.getString("msg")
        tvNoDeviceFoundMsg.text = msg
        Log.e("device","device")
    }
}
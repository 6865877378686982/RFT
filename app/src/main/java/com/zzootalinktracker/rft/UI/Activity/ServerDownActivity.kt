package com.zzootalinktracker.rft.UI.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.zzootalinktracker.rft.R

class ServerDownActivity : AppCompatActivity() {
    private var is_splash_activity = false
    private var is_internet_layout = false
    private lateinit var btn_retry_server_down :Button
    private lateinit var btn_retry :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_down)

        btn_retry_server_down = findViewById(R.id.btn_retry_server_down)
        is_internet_layout = intent.extras!!.getBoolean("is_internet_layout")
        is_splash_activity = intent.extras!!.getBoolean("is_splash_activity")

    }
}
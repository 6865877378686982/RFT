package com.zzootalinktracker.rft.UI.Fragment.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.Utils.*

class DeviceNotConfiguredScreen : AppCompatActivity() {
    private lateinit var noDeviceConfiguredLayout: RelativeLayout
    private lateinit var noDeviceFoundLayout: RelativeLayout
    private lateinit var noInternetFoundLayout: RelativeLayout
    private lateinit var noServerFoundLayout: RelativeLayout
    private lateinit var noUserConfiguredLayout: RelativeLayout
    private lateinit var deactivatedLayout: RelativeLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devive_not_configured_screen)

        initView()
        updateUI()

    }

    private fun initView() {
        noDeviceConfiguredLayout = findViewById(R.id.noDeviceConfiguredLayout)
        noDeviceFoundLayout = findViewById(R.id.noDeviceFoundLayout)
        noInternetFoundLayout = findViewById(R.id.noInternetFoundLayout)
        noServerFoundLayout = findViewById(R.id.noServerFoundLayout)
        noUserConfiguredLayout = findViewById(R.id.noUserConfiguredLayout)
        deactivatedLayout = findViewById(R.id.deactivatedLayout)

    }

    private fun updateUI(){
        val type = intent.extras!!.getInt("type")
        when(type){
            //No Internet
            1 ->{
                noInternetFoundLayout.visibility = View.VISIBLE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.GONE

            }
            //No Server
            2 ->{
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.VISIBLE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.GONE

            }
            //DEACTIVE_DEVICE
            3 ->{
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.VISIBLE
            }
            //DEVICE_NOT_CONFIGURED

            4 -> {
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.VISIBLE
            }

         // USER_NOT_CONFIGURED
            5 -> {
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.VISIBLE
                deactivatedLayout.visibility = View.GONE
            }

        }

    }
}
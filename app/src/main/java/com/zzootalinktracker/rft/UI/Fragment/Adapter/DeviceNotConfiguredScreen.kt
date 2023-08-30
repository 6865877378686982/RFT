package com.zzootalinktracker.rft.UI.Fragment.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.zzootalinktracker.android.Ui.Activity.SplashActivity
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.Utils.*

class DeviceNotConfiguredScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var noDeviceConfiguredLayout: RelativeLayout
    private lateinit var noDeviceFoundLayout: RelativeLayout
    private lateinit var noInternetFoundLayout: RelativeLayout
    private lateinit var noServerFoundLayout: RelativeLayout
    private lateinit var noUserConfiguredLayout: RelativeLayout
    private lateinit var deactivatedLayout: RelativeLayout
    private lateinit var btnTryAgain: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devive_not_configured_screen)

        initView()


    }

    private fun initView() {
        noDeviceConfiguredLayout = findViewById(R.id.noDeviceConfiguredLayout)
        noDeviceFoundLayout = findViewById(R.id.noDeviceFoundLayout)
        noInternetFoundLayout = findViewById(R.id.noInternetFoundLayout)
        noServerFoundLayout = findViewById(R.id.noServerFoundLayout)
        noUserConfiguredLayout = findViewById(R.id.noUserConfiguredLayout)
        deactivatedLayout = findViewById(R.id.deactivatedLayout)
        btnTryAgain = findViewById(R.id.btnTryAgain)
        btnTryAgain.setOnClickListener(this)
        updateUI()
    }

    private fun updateUI(){
        val type = intent.extras!!.getInt("type")
        when(type){
            //No Internet
            NO_INTERNET ->{
                noInternetFoundLayout.visibility = View.VISIBLE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.GONE

            }
            //No Server
            NO_SERVER ->{
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.VISIBLE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.GONE

            }
            //DEACTIVE_DEVICE
            DEACTIVE_DEVICE ->{
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.VISIBLE
            }
            //DEVICE_NOT_CONFIGURED

            DEVICE_NOT_CONFIGURED -> {
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.VISIBLE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.GONE
                deactivatedLayout.visibility = View.GONE
            }

         // USER_NOT_CONFIGURED
            USER_NOT_CONFIGURED -> {
                noInternetFoundLayout.visibility = View.GONE
                noDeviceConfiguredLayout.visibility = View.GONE
                noDeviceFoundLayout.visibility = View.GONE
                noServerFoundLayout.visibility = View.GONE
                noUserConfiguredLayout.visibility = View.VISIBLE
                deactivatedLayout.visibility = View.GONE
            }

        }

    }

    override fun onClick(v: View?) {
        when(v){
            btnTryAgain -> {
                finish()
                val intent = Intent(applicationContext,SplashActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
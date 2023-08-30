package com.zzootalinktracker.android.Ui.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.MainActivity
import com.zzootalinktracker.rft.UI.Fragment.Adapter.DeviceNotConfiguredScreen
import com.zzootalinktracker.rft.UI.Fragment.Model.GetDeviceDriverInfoModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import com.zzootalinktracker.rft.Utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SplashActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var sessionManagerEmailSave: SessionManagerEmailSave


    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {
        sessionManager = SessionManager(applicationContext)
        sessionManagerEmailSave = SessionManagerEmailSave(applicationContext)
        askRequestsPermission()
    }


    private fun askRequestsPermission() {
        if (ContextCompat.checkSelfPermission(
                this@SplashActivity, Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity, Manifest.permission.READ_PHONE_STATE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@SplashActivity, arrayOf(
                        Manifest.permission.READ_PHONE_STATE
                    ), 1001
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@SplashActivity, arrayOf(
                        Manifest.permission.READ_PHONE_STATE
                    ), 1001
                )
            }
        } else {
            getIMEI()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (((ContextCompat.checkSelfPermission(
                            this@SplashActivity, Manifest.permission.READ_PHONE_STATE
                        ) === PackageManager.PERMISSION_GRANTED))

                    ) {
                        getIMEI()
                    } else {
                        askRequestsPermission()
                    }
                } else {
                    askRequestsPermission()
                }
                return
            }
        }
    }


    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getIMEI() {

        val version = Build.VERSION.SDK_INT
        try {
            val telephonyManager =
                this@SplashActivity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (version < Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(
                        this@SplashActivity, Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val imei = telephonyManager.deviceId
                    sessionManager.saveIMEI(imei.toString())
                    checkLoginExist()
                }
            } else {
                val androidId = Settings.Secure.getString(
                    this.contentResolver, Settings.Secure.ANDROID_ID
                )
                sessionManager.saveIMEI(androidId)
                checkLoginExist()
            }
        } catch (e: Exception) {
            Log.e("splash", e.message.toString())
        }
    }

    private fun checkLoginExist() {
        rftLogin()
    }


    private fun rftLogin() {
        try {
            if (isOnline(applicationContext)) {
                try {
                    val version = Build.VERSION.SDK_INT
                    var isVersionAbove28 = if (version < Build.VERSION_CODES.Q) 0 else 1
                    val apiInterface = ApiInterface.createForRFT()
                    var imei = sessionManager.getIMEI()
                    isVersionAbove28 = 1
                    imei = "9d575a53786a98c8"
                 //   imei = "869196033386166"
                 /*   imei = "350675293717976"*/

                    apiInterface.getDeviceDriverInfo(
                        isVersionAbove28,
                        if (isVersionAbove28 == 0) imei ?: "" else "",
                        if (isVersionAbove28 == 1) imei ?: "" else ""
                    ).enqueue(object : Callback<GetDeviceDriverInfoModel> {
                        override fun onResponse(
                            call: Call<GetDeviceDriverInfoModel>,
                            response: Response<GetDeviceDriverInfoModel>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == SUCCESS_STATUS_EDGE) {
                                    val driverId = response.body()!!.data.driverId
                                    if (driverId != null) {
                                        sessionManager.saveRftDriverId("005087")
                                    }
                                    val firstName = response.body()!!.data.firstName
                                    val lasttName = response.body()!!.data.lastName
                                    var name = ""
                                    if (firstName != null) {
                                        name = firstName
                                    }
                                    if (lasttName != null) {
                                        name = "$name $lasttName"
                                    }
                                    sessionManager.saveDriverName(name)
                                    sessionManager.saveLoginTimeStamp(getCurrentDateTime24Hour())
                                    val intent = Intent(
                                        this@SplashActivity, MainActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()
                                } else {
                                    goToDeviceNotConfiguredScreen(DEVICE_NOT_CONFIGURED)
                                    addFlurryErrorEvents(
                                        this@SplashActivity.localClassName,
                                        "GetDeviceDriverInfo",
                                        sessionManager.getIMEI(),
                                        version.toString(),
                                        response.message(),
                                        "apiUnsuccess"
                                    )
                                }
                            } else {
                                addFlurryErrorEvents(
                                    this@SplashActivity.localClassName,
                                    "GetDeviceDriverInfo",
                                    sessionManager.getIMEI(),
                                    version.toString(),
                                    response.message(),
                                    "apiUnsuccess"
                                )
                                goToDeviceNotConfiguredScreen(NO_SERVER)
                            }
                        }

                        override fun onFailure(call: Call<GetDeviceDriverInfoModel>, t: Throwable) {
                            addFlurryErrorEvents(
                                this@SplashActivity.localClassName,
                                "GetDeviceDriverInfo",
                                sessionManager.getIMEI(),
                                version.toString(),
                                t.message.toString(),
                                "apiFailure"
                            )
                            goToDeviceNotConfiguredScreen(NO_SERVER)
                        }

                    })

                } catch (e: Exception) {
                    goToDeviceNotConfiguredScreen(NO_SERVER)
                }
            }else{
                goToDeviceNotConfiguredScreen(NO_INTERNET)
            }
        } catch (e: Exception) {
            goToDeviceNotConfiguredScreen(NO_SERVER)
        }
    }


    private fun goToDeviceNotConfiguredScreen(type: Int) {
        val intent = Intent(
            this@SplashActivity, DeviceNotConfiguredScreen::class.java
        )
        intent.putExtra("type", type)
        startActivity(intent)
        finish()
    }




}



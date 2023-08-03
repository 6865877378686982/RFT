package com.zzootalinktracker.android.Ui.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.UI.Activity.MainActivity
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Fragment.Adapter.DeviceNotConfiguredScreen
import com.zzootalinktracker.rft.UI.Activity.Model.RftLoginModel
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
        if (sessionManager.getApiHash() == "") {
            rftLogin()
        } else {
            loadScreen()
        }
    }

    private fun rftLogin() {
        try {
            if (isOnline(applicationContext)) {
                try {
                    val version = Build.VERSION.SDK_INT
                    var isVersionAbove28 = 0
                    if (version < Build.VERSION_CODES.Q) {
                        isVersionAbove28 = 0
                    } else {
                        isVersionAbove28 = 1
                    }
                    Log.e("imei------223", sessionManager.getIMEI())
                    ApiInterface.create().rftLogin(sessionManager.getIMEI(), isVersionAbove28)
                        .enqueue(object : Callback<RftLoginModel> {
                            override fun onResponse(
                                call: Call<RftLoginModel>, response: Response<RftLoginModel>
                            ) {
                                if (response.isSuccessful) {
                                    if (response.body()!!.status == SUCCESS_STATUS) {
                                        if (response.body()!!.data.active == 0) {
                                            // show new screen -> msg => Please active device from link
                                            goToDeviceNotConfiguredScreen(DEACTIVE_DEVICE)
                                            return
                                        }
                                        val rftDriver = response.body()!!.data.rft_driver
                                        if (rftDriver != null) {
                                            val apiHash = rftDriver.user_api_hash
                                            val deviceId = response.body()!!.data.id
                                            val userId = response.body()!!.data.rft_driver.id
                                            val email = response.body()!!.data.rft_driver.email
                                            val rftDriverId =
                                                response.body()!!.data.rft_driver.rft_driver_id
                                            sessionManager.saveApiHash(apiHash)
                                            sessionManager.saveDeviceId(deviceId)
                                            sessionManager.saveUserId(userId)
                                            sessionManager.saveRftDriverId(rftDriverId)
                                            sessionManager.saveUserEmail(email)
                                            sessionManager.saveLoginTimeStamp(
                                                getCurrentDateTime24Hour()
                                            )
                                            val intent = Intent(
                                                this@SplashActivity, MainActivity::class.java
                                            )
                                            startActivity(intent)
                                        } else {
                                            // show new screen - msg => your device is not configured
                                            goToDeviceNotConfiguredScreen(USER_NOT_CONFIGURED)
                                        }
                                    } else {
                                        // show new screen - msg => your device is not configured
                                        goToDeviceNotConfiguredScreen(DEVICE_NOT_CONFIGURED)
                                    }
                                } else {
                                    goToDeviceNotConfiguredScreen(NO_SERVER)
                                }

                            }

                            override fun onFailure(call: Call<RftLoginModel>, t: Throwable) {
                                goToDeviceNotConfiguredScreen(DEACTIVE_DEVICE)
                            }


                        })
                } catch (e: Exception) {
                    Log.e("hi", e.toString())
                }
            } else {
                goToDeviceNotConfiguredScreen(NO_INTERNET)
            }
        } catch (e: Exception) {
            Log.e("spash", e.message.toString())
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

    private fun goToLoginScreen() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun loadScreen() {

        val time = sessionManager.getLoginTimeStamp()
        if (time == "") {
            rftLogin()
        } else {
            val dateFormatter = getDateFormationOnly()
            if (dateFormatter.parse(time) == dateFormatter.parse(getCurrentDateOnly())) {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1000)
            } else {
                rftLogin()
            }
        }
    }

}


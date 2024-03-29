package com.zzootalinktracker.android.Ui.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.MainActivity
import com.zzootalinktracker.rft.UI.Activity.Model.PushNotificationDataModel
import com.zzootalinktracker.rft.UI.Activity.Model.ResponseModel
import com.zzootalinktracker.rft.UI.Activity.DeviceNotConfiguredScreen
import com.zzootalinktracker.rft.UI.Fragment.Model.GetDeviceDriverInfoModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerIdsHavingCurrentTripNotNullModel
import com.zzootalinktracker.rft.Utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SplashActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var sessionManagerEmailSave: SessionManagerEmailSave
    val PERMISSION_REQUEST_CODE = 112

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        /*  getTrailerIdsHavingCurrentTripNotNull()*/
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
            ) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity, Manifest.permission.READ_PHONE_STATE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@SplashActivity, arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.POST_NOTIFICATIONS
                    ), 1001
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@SplashActivity, arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.POST_NOTIFICATIONS
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
        rftLogin("")
        //  getTrailerIdsHavingCurrentTripNotNull()
    }

    private fun getTrailerIdsHavingCurrentTripNotNull() {
        try {
            if (isOnline(applicationContext)) {
                try {
                    ApiInterface.createForRFT().getTrailerIdsHavingCurrentTripNotNull()
                        .enqueue(object : Callback<GetTrailerIdsHavingCurrentTripNotNullModel> {
                            override fun onResponse(
                                call: Call<GetTrailerIdsHavingCurrentTripNotNullModel>,
                                response: Response<GetTrailerIdsHavingCurrentTripNotNullModel>
                            ) {
                                if (response.isSuccessful) {
                                    var trailerId = response.body()!!.data[1].trailerId
                                    rftLogin(trailerId)
                                }
                            }

                            override fun onFailure(
                                call: Call<GetTrailerIdsHavingCurrentTripNotNullModel>,
                                t: Throwable
                            ) {
                                t.printStackTrace()
                            }

                        })

                } catch (e: Exception) {

                }
            }
        } catch (e: Exception) {

        }
    }


    private fun rftLogin(trailerId: String) {
        try {
            if (isOnline(applicationContext)) {
                try {
                    val version = Build.VERSION.SDK_INT
                    val isVersionAbove28 = if (version < Build.VERSION_CODES.Q) 0 else 1
                    val apiInterface = ApiInterface.createForRFT()
                    var imei = sessionManager.getIMEI()
                     imei = "d9c1133043a81b06"

                    var apiPath = ""
                    if (LIVE_BUILD)
                        apiPath = "GetDeviceDriverInfoLive"
                    else
                        apiPath = "GetDeviceDriverInfo"

                    apiInterface.getDeviceDriverInfo(apiPath,
                        isVersionAbove28, imei, imei
                    ).enqueue(object : Callback<GetDeviceDriverInfoModel> {
                        override fun onResponse(
                            call: Call<GetDeviceDriverInfoModel>,
                            response: Response<GetDeviceDriverInfoModel>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == SUCCESS_STATUS_EDGE) {
                                    val driverId = response.body()!!.data.driverId
                                    if (driverId != null) {
                                        sessionManager.saveRftDriverId(driverId)
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
                                    var token = response.body()!!.data.token
                                   /* if(token!=null){
                                        sessionManager.saveStoredToken(token)
                                    }else{
                                        sessionManager.saveStoredToken("")
                                    }*/
                                    generateFireBaseToken()

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
            } else {
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

    private fun generateFireBaseToken(){
        val storedToken = sessionManager.getStoredToken()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                val intent = Intent(
                    this@SplashActivity, MainActivity::class.java
                )
                startActivity(intent)
                finish()
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result


            var driverId = sessionManager.getRftDriverId()
            try {
                val model = PushNotificationDataModel(driverId, token)

                ApiInterface.createForRFT().pushNotificatiionUpdateToken(model)
                    .enqueue(object : retrofit2.Callback<ResponseModel> {
                        override fun onResponse(
                            call: retrofit2.Call<ResponseModel>,
                            response: retrofit2.Response<ResponseModel>
                        ) {

                            if (response.isSuccessful) {


                            } else {


                            }
                            val intent = Intent(
                                this@SplashActivity, MainActivity::class.java
                            )
                            sessionManager.saveStoredToken(token)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(call: retrofit2.Call<ResponseModel>, t: Throwable) {
                            val intent = Intent(
                                this@SplashActivity, MainActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        }
                    })

            } catch (e: Exception) {
                val intent = Intent(
                    this@SplashActivity, MainActivity::class.java
                )
                startActivity(intent)
                finish()
                e.toString()
            }
        })
    }


}



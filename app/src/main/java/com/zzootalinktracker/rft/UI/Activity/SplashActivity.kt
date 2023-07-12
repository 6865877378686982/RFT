package com.zzootalinktracker.android.Ui.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.MainActivity
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.LoginActivity
import com.zzootalinktracker.rft.UI.Activity.Model.GetLasLoginDeviceHistoryModel
import com.zzootalinktracker.rft.Utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val PREFS_NAME = "MyPrefs"
    private val LAST_ACTIVITY_KEY = "lastActivity"

    private lateinit var sessionManager: SessionManager
    private lateinit var sessionManagerEmailSave: SessionManagerEmailSave

    @RequiresApi(Build.VERSION_CODES.Q)
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.CAMERA
    )
    private val PERMISSIONS_REQUEST_CODE = 1


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sessionManager = SessionManager(applicationContext)
        sessionManagerEmailSave = SessionManagerEmailSave(applicationContext)

        if (ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.CAMERA
            ) !==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.READ_PHONE_STATE
            ) !==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.CAMERA
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.READ_PHONE_STATE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ), 1001
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ), 1001
                )
            }
        } else {
            getIMEI()
        }


    }

    override fun onResume() {
        super.onResume()
        /* val lastActivityName = sharedPrefs.getString(LAST_ACTIVITY_KEY, null)
         if (!lastActivityName.isNullOrEmpty()) {
             try {
                 val lastActivityClass = Class.forName(lastActivityName)
                 val intent = Intent(this, lastActivityClass)
                 startActivity(intent)
                 finish()
             } catch (e: ClassNotFoundException) {
                 e.printStackTrace()
             }
         }*/

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.CAMERA
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                        &&
                        (ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                        &&
                        (ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.READ_PHONE_STATE
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                        &&
                        (ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                        &&
                        (ContextCompat.checkSelfPermission(
                            this@SplashActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) ===
                                PackageManager.PERMISSION_GRANTED)

                    ) {
                        getIMEI()
                    }
                } else {

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
                        this@SplashActivity,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val IMEINumber = telephonyManager.deviceId
                    if (!sessionManager.getBooleanData(QR_CODE_LOGIN)) {
                        sessionManager.saveIMEI(IMEINumber.toString())
                    }
                    checkLoginExist()
                }
            } else {
                if (!sessionManager.getBooleanData(QR_CODE_LOGIN)) {
                    val androidId = Settings.Secure.getString(
                        this.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    sessionManager.saveIMEI(androidId)
                    Log.e("Imei", "Imei")
                }
                /*    sessionManager.saveIMEI("869196033412327")*/
                checkLoginExist()
            }
        } catch (e: Exception) {
            Log.e("logineroor", e.message!!)
        }
    }

    private fun checkLoginExist() {
        try {
            if (sessionManager.getStringData(API_HASH) == "") {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    if (sessionManagerEmailSave.getIntroSliderStatus() != "") {
                        /*val intent = Intent(applicationContext, LoginScreen::class.java)
                        startActivity(intent)
                        finish()*/
                        goToLoginScreen()
                    } else {
                        goToLoginScreen()
                        /*  val intent = Intent(applicationContext, LoginActivity::class.java)
                          intent.putExtra("isComeFromHome", false)
                          startActivity(intent)
                          finish()*/
                    }
                }, 2000)
            } else {
                loadScreen()
            }
        } catch (e: java.lang.Exception) {
            finish()
        }
    }

    private fun goToLoginScreen() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent =
                Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun loadScreen() {

        val time = sessionManager.getStringData(LOGIN_TIMESTAMP)
        if (time == "") {
            val handler = Handler()
            handler.postDelayed(Runnable {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
            checkUserLoginHistory()

        } else {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
            if (dateFormatter.parse(time) == dateFormatter.parse(getCurrentDateOnly())) {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000)
                checkUserLoginHistory()
            } else {
                sessionManager.logOut()
                getIMEI()
            }
        }
    }

    private fun checkUserLoginHistory() {
        ApiInterface.create().getLastLoginDeviceHistory(
            sessionManager.getStringData(API_HASH), sessionManager.getIntData(
                DEVICE_ID
            ).toString()
        ).enqueue(object : Callback<GetLasLoginDeviceHistoryModel> {
            override fun onResponse(
                call: Call<GetLasLoginDeviceHistoryModel>,
                response: Response<GetLasLoginDeviceHistoryModel>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == SUCCESS_STATUS) {
                        val userId = response.body()!!.data.user_id
                        val device_id = response.body()!!.data.device_id
                        if (userId == sessionManager.getIntData(USER_ID)
                                .toString() && device_id == sessionManager.getIntData(
                                DEVICE_ID
                            ).toString()
                        ) {
                            goToHome()

                        } else {
                            sessionManager.logOut()
                            getIMEI()
                        }
                    } else {
                        goToLoginScreen()
                    }
                } else {
                    goToLoginScreen()
                }
            }

            override fun onFailure(call: Call<GetLasLoginDeviceHistoryModel>, t: Throwable) {
                goToLoginScreen()
            }

        })
    }

    private fun goToHome() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent =
                Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}


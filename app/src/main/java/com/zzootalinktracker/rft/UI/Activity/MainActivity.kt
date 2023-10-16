package com.zzootalinktracker.rft.UI.Activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.Service.GetTrailerTagStatusService
import com.zzootalinktracker.rft.UI.Activity.Model.PushNotificationDataModel
import com.zzootalinktracker.rft.UI.Activity.Model.ResponseModel
import com.zzootalinktracker.rft.UI.Fragment.HomeFragment
import com.zzootalinktracker.rft.UI.Fragment.LogHistoryFragment
import org.json.JSONObject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fragmentHome: HomeFragment
    private lateinit var sessionManager: SessionManager
    private lateinit var fragmentLogHistory: LogHistoryFragment

    private var fm: FragmentManager? = null

    private var CURRENT_TAB = ""
    private val homeTab = "home"
    private val historyTab = "history"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        fragmentHome = HomeFragment()
        fragmentLogHistory = LogHistoryFragment()


        fm = supportFragmentManager
        fm!!.beginTransaction().add(R.id.fragment_container, fragmentHome).commit()
        fm!!.beginTransaction().add(R.id.fragment_container, fragmentLogHistory).commit()
        fm!!.beginTransaction().show(fragmentHome).hide(fragmentLogHistory).commit()
        CURRENT_TAB = homeTab

        startTrailerTagStatusService()
        sessionManager = SessionManager(applicationContext)

        val storedToken = sessionManager.getStoredToken()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            var savedToken = sessionManager.saveStoredToken(token)
            var driverId = sessionManager.getRftDriverId()
            /* pushNotificationData(driverId,token)*/
            pushNotificationData()

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(ContentValues.TAG, msg)
            //      Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

    }

    private fun pushNotificationData() {
        try {

            val paramObject = JSONObject()
            paramObject.put("DriverId", sessionManager.getRftDriverId())
            paramObject.put("Token", sessionManager.getStoredToken())

            val gsonObject = JsonParser().parse(paramObject.toString()) as JsonObject
            var driverId = sessionManager.getRftDriverId()
            var token = sessionManager.getStoredToken()

            val model = PushNotificationDataModel(driverId, token)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                ApiInterface.createForRFT().pushNotificatiionUpdateToken(model)
                    .enqueue(object : retrofit2.Callback<ResponseModel> {
                        override fun onResponse(
                            call: retrofit2.Call<ResponseModel>,
                            response: retrofit2.Response<ResponseModel>
                        ) {

                            if (response.isSuccessful) {


                            } else {


                            }
                        }

                        override fun onFailure(call: retrofit2.Call<ResponseModel>, t: Throwable) {
                            t.message
                        }
                    })
            }


        } catch (e: Exception) {

            e.toString()
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                fm!!.beginTransaction().show(fragmentHome).hide(fragmentLogHistory).commit()
                CURRENT_TAB = homeTab
            }

            R.id.navigation_logHistory -> {
                fm!!.beginTransaction().show(fragmentLogHistory).hide(fragmentHome).commit()
                CURRENT_TAB = historyTab

            }
        }
        return true
    }


    private fun startTrailerTagStatusService() {
        try {
            if (!isMyServiceRunning(GetTrailerTagStatusService::class.java)) {
                val i = Intent(this@MainActivity, GetTrailerTagStatusService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startService(i)
                } else {
                    startService(i)
                }
            }
        } catch (e: Exception) {

        }
    }

    override fun onDestroy() {
        try {

            val myService = Intent(this@MainActivity, GetTrailerTagStatusService::class.java)
            stopService(myService)
        } catch (e: Exception) {

        }
        super.onDestroy()
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        try {

            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

}


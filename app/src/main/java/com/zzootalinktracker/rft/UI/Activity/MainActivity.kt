package com.zzootalinktracker.rft.UI.Activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.Service.GetTrailerTagStatusService
import com.zzootalinktracker.rft.UI.Fragment.HomeFragment
import com.zzootalinktracker.rft.UI.Fragment.LogHistoryFragment
import com.zzootalinktracker.rft.Utils.isOnline

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fragmentHome: HomeFragment
    private lateinit var fragmentLogHistory: LogHistoryFragment

    private var fm: FragmentManager? = null

    private var CURRENT_TAB = ""
    private val homeTab = "home"
    private val historyTab = "history"

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

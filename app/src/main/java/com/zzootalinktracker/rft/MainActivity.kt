package com.zzootalinktracker.rft

import TrailerAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerModel
import com.zzootalinktracker.rft.UI.Fragment.HomeFragment
import com.zzootalinktracker.rft.UI.Fragment.LogHistoryFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fragmentHome: HomeFragment
    private lateinit var fragmentLogHistory: LogHistoryFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        fragmentHome = HomeFragment()
        fragmentLogHistory = LogHistoryFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragmentHome)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(fragmentHome)
                return true
            }
            R.id.navigation_logHistory -> {
                switchFragment(fragmentLogHistory)
                return true
            }
        }
        return false
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}

package com.zzootalinktracker.rft

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.UI.Activity.Adapter.TrailerAdapter
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

private lateinit var recycler_view:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view = findViewById(R.id.recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<TrailerModel>()
        data.add(TrailerModel())
        val adapter = TrailerAdapter(data)
        recycler_view.adapter = adapter


    }




}
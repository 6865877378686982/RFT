package com.zzootalinktracker.rft.UI.Fragment

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.Service.GetTrailerTagStatusService
import com.zzootalinktracker.rft.Service.Adapter.ChillerAdapter
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import com.zzootalinktracker.rft.Utils.isOnline
import kotlin.collections.ArrayList


class HomeFragment() : Fragment(), View.OnClickListener {


    private lateinit var rvChiller: RecyclerView
    private lateinit var tvDriverName: TextView
    private lateinit var mainLayoutHomeFragment: RelativeLayout
    private lateinit var sessionManager: SessionManager
    private lateinit var trailerList: ArrayList<GetTrailerTagsStatusModel.Data>
    private lateinit var adapter: ChillerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var viewLayout: View

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLayout = inflater.inflate(R.layout.fragment_home, container, false)
        initView()



        return viewLayout
    }

    private fun initView() {
        try {
            sessionManager = SessionManager(context!!)
            rvChiller = viewLayout.findViewById(R.id.rvChiller)
            mainLayoutHomeFragment = viewLayout.findViewById(R.id.mainLayoutHomeFragment)
            progressBar = viewLayout.findViewById(R.id.progressBar)
            progressBar.visibility = View.GONE
            tvDriverName = viewLayout.findViewById(R.id.tvDriverName)

            tvDriverName.text = "Hi, " + sessionManager.getUserEmail()
            rvChiller.layoutManager = LinearLayoutManager(context!!)

            setAdapter()
        } catch (e: Exception) {
            Log.e("HomeFragment", e.message.toString())
        }
    }

    private fun setAdapter() {
        try {
            trailerList = ArrayList()
            adapter = ChillerAdapter(context!!, trailerList)
            rvChiller.adapter = adapter
            changeProgressColor()
        } catch (e: Exception) {
            Log.e("HomeFragment", e.message.toString())
        }

    }

    private val trailerStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "TRAILER_STATUS_UPDATE") {
                val intentData = intent.getStringExtra("trailerList")
                val gson = Gson()
                val model = gson.fromJson(intentData, GetTrailerTagsStatusModel::class.java)
                trailerList.clear()
                trailerList.addAll(model.data)
                progressBar.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun changeProgressColor() {
        val progressColor = resources.getColor(R.color.red_rft)
        val colorFilter = PorterDuffColorFilter(progressColor, PorterDuff.Mode.SRC_IN)
        progressBar.indeterminateDrawable.colorFilter = colorFilter
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
    }


    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter("TRAILER_STATUS_UPDATE")
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(trailerStatusReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(trailerStatusReceiver)
    }

    override fun onClick(v: View?) {

    }
}
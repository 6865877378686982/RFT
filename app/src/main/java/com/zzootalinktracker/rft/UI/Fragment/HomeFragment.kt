package com.zzootalinktracker.rft.UI.Fragment

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.Service.GetTrailerTagStatusService
import com.zzootalinktracker.rft.UI.Activity.Adapter.ChillerAdapter
import com.zzootalinktracker.rft.UI.Activity.LoginActivity
import com.zzootalinktracker.rft.UI.Activity.Model.ChillerModel
import com.zzootalinktracker.rft.UI.Activity.Model.GetLasLoginDeviceHistoryModel
import com.zzootalinktracker.rft.UI.Activity.Model.PadModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import com.zzootalinktracker.rft.Utils.E_MAIL
import com.zzootalinktracker.rft.Utils.SUCCESS_STATUS
import com.zzootalinktracker.rft.Utils.SUCCESS_STATUS_EDGE
import com.zzootalinktracker.rft.Utils.isOnline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var rvChiller: RecyclerView
    private lateinit var tvDriverName: TextView
    private lateinit var btnLogout: ImageView
    private lateinit var sessionManagerEmailSave: SessionManagerEmailSave
    private lateinit var sessionManager: SessionManager
    private lateinit var trailerList: ArrayList<GetTrailerTagsStatusModel.Data>
    private lateinit var service: GetTrailerTagStatusService
    private lateinit var adapter: ChillerAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewLayout = inflater.inflate(R.layout.fragment_home, container, false)
        sessionManagerEmailSave = SessionManagerEmailSave(context!!)
        sessionManager = SessionManager(context!!)
        rvChiller = viewLayout.findViewById(R.id.rvChiller)
        btnLogout = viewLayout.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener(this)
        tvDriverName = viewLayout.findViewById(R.id.tvDriverName)
        tvDriverName.text = "Hi," + sessionManagerEmailSave.getEmail()
        rvChiller.layoutManager = LinearLayoutManager(context!!)
        trailerList = ArrayList()
        adapter = ChillerAdapter(context!!,trailerList)
        rvChiller.adapter = adapter
        startTrailerTagStatusService()

        return viewLayout
    }

    private val trailerStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "TRAILER_STATUS_UPDATE") {
                val inentData = intent.getStringExtra("trailerList")
                val gson = Gson()
                var model = gson.fromJson(inentData, GetTrailerTagsStatusModel::class.java)
                trailerList.clear()
                trailerList.addAll(model.data)
                adapter.notifyDataSetChanged()
                Log.e("trailerLlist", "trailerLlist")


            }
        }
    }

    private fun startTrailerTagStatusService() {
        try {
            if (!isMyServiceRunning(GetTrailerTagStatusService::class.java)) {
                val i = Intent(requireContext(), GetTrailerTagStatusService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    /*    requireContext().startForegroundService(i)*/
                    requireContext().startService(i)
                } else {
                    requireContext().startService(i)
                }
            }
        } catch (e: Exception) {

        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        try {
            val context = requireContext()
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
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
        when (v) {
            btnLogout -> {
                /* var intent = Intent(context,LoginActivity::class.java)
                 startActivity(intent)*/
                sessionManager.logOut()
            }
        }
    }
}
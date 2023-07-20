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


class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var rvChiller: RecyclerView
    private lateinit var tvDriverName: TextView
    private lateinit var btnLogout: ImageView
    private lateinit var mainLayoutHomeFragment: RelativeLayout
    private lateinit var noInternetLayoutHomeFragment: RelativeLayout
    private lateinit var noDataLayoutHomeFragment: RelativeLayout
    private lateinit var sessionManagerEmailSave: SessionManagerEmailSave
    private lateinit var sessionManager: SessionManager
    private lateinit var trailerList: ArrayList<GetTrailerTagsStatusModel.Data>
    private lateinit var service: GetTrailerTagStatusService
    private lateinit var adapter: ChillerAdapter
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewLayout = inflater.inflate(R.layout.fragment_home, container, false)
        sessionManagerEmailSave = SessionManagerEmailSave(context!!)
        sessionManager = SessionManager(context!!)
        rvChiller = viewLayout.findViewById(R.id.rvChiller)
        mainLayoutHomeFragment = viewLayout.findViewById(R.id.mainLayoutHomeFragment)
        noInternetLayoutHomeFragment = viewLayout.findViewById(R.id.noInternetLayoutHomeFragment)
        noDataLayoutHomeFragment = viewLayout.findViewById(R.id.noDataLayoutHomeFragment)
        btnLogout = viewLayout.findViewById(R.id.btnLogout)
        progressBar = viewLayout.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        btnLogout.setOnClickListener(this)
        tvDriverName = viewLayout.findViewById(R.id.tvDriverName)
        tvDriverName.text = "Hi," + sessionManagerEmailSave.getEmail()
        rvChiller.layoutManager = LinearLayoutManager(context!!)
        trailerList = ArrayList()
        adapter = ChillerAdapter(context!!,trailerList)
        rvChiller.adapter = adapter
        startTrailerTagStatusService()
        changeProgressColor()

        return viewLayout
    }

    private val trailerStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (isOnline(context)){
                noInternetLayoutHomeFragment.visibility = View.GONE
                mainLayoutHomeFragment.visibility = View.VISIBLE
                if (intent.action == "TRAILER_STATUS_UPDATE") {
                    val intentData = intent.getStringExtra("trailerList")
                    val gson = Gson()
                    var model = gson.fromJson(intentData, GetTrailerTagsStatusModel::class.java)
                    trailerList.clear()
                    trailerList.addAll(model.data)
                    progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                    Log.e("trailerLlist", "trailerLlist")




                }
            }else{
                noInternetLayoutHomeFragment.visibility = View.VISIBLE
                mainLayoutHomeFragment.visibility = View.GONE
            }

        }
    }

    private fun changeProgressColor(){
        val progressColor = resources.getColor(R.color.red_rft)
        val colorFilter = PorterDuffColorFilter(progressColor, PorterDuff.Mode.SRC_IN)
        progressBar.indeterminateDrawable.colorFilter = colorFilter
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
    }

    private fun startTrailerTagStatusService() {
        if (isOnline(context!!)){
            noInternetLayoutHomeFragment.visibility = View.GONE
            mainLayoutHomeFragment.visibility = View.VISIBLE
            try {
                if (!isMyServiceRunning(GetTrailerTagStatusService::class.java)) {
                    val i = Intent(requireContext(), GetTrailerTagStatusService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        /*    requireContext().startForegroundService(i)*/
//
                        progressBar.visibility = View.VISIBLE
                        requireContext().startService(i)
                    } else {

                        requireContext().startService(i)
                    }
                }
            } catch (e: Exception) {

            }
        }else{
            noInternetLayoutHomeFragment.visibility = View.VISIBLE
            mainLayoutHomeFragment.visibility = View.GONE
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
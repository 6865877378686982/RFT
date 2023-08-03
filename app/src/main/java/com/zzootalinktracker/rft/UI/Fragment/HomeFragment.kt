package com.zzootalinktracker.rft.UI.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Fragment.Adapter.ChillerAdapter
import com.zzootalinktracker.rft.UI.Fragment.Adapter.StoredAlertAdapter
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import com.zzootalinktracker.rft.UI.Fragment.Model.TagModel
import com.zzootalinktracker.rft.Utils.SUCCESS_STATUS_EDGE
import com.zzootalinktracker.rft.Utils.getCurrentDateTime24Hour


class HomeFragment() : Fragment(), View.OnClickListener {


    private lateinit var rvChiller: RecyclerView
    private lateinit var scrollView: NestedScrollView
    private lateinit var tvDriverName: TextView
    private lateinit var sessionManager: SessionManager
    private lateinit var trailerList: ArrayList<GetTrailerTagsStatusModel.Data>
    private lateinit var adapter: ChillerAdapter
    private lateinit var storedAlertAdapter: StoredAlertAdapter
    private lateinit var progressBar: LinearLayout
    private lateinit var viewLayout: View
    private lateinit var tvLastRefreshed: TextView
    private lateinit var tagModelArray: ArrayList<TagModel>
    private lateinit var stoedAlertDialog: Dialog

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
            scrollView = viewLayout.findViewById(R.id.scrollView)
            progressBar = viewLayout.findViewById(R.id.progressBar)

            tvDriverName = viewLayout.findViewById(R.id.tvDriverName)
            tvLastRefreshed = viewLayout.findViewById(R.id.tvLastRefreshed)

            tvDriverName.text = "Hi, " + sessionManager.getUserEmail()
            rvChiller.layoutManager = LinearLayoutManager(context!!)
            showHideProgressBar(true)
            setAdapter()
        } catch (e: Exception) {
            Log.e("HomeFragment", e.message.toString())
        }
    }

    private fun showHideProgressBar(yes: Boolean) {
        if (yes) {
            progressBar.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        }

    }

    private fun setAdapter() {
        try {
            trailerList = ArrayList()
            adapter = ChillerAdapter(context!!, trailerList)
            rvChiller.adapter = adapter
        } catch (e: Exception) {
            Log.e("HomeFragment", e.message.toString())
        }

    }

    private val trailerStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "TRAILER_STATUS_UPDATE") {
                sessionManager.saveTimestamp(getCurrentDateTime24Hour())
                val lastRefreshTime = sessionManager.getTimestamp()
                tvLastRefreshed.text = "Last Refreshed at: $lastRefreshTime"
                val intentData = intent.getStringExtra("trailerList")
                val gson = Gson()
                val model = gson.fromJson(intentData, GetTrailerTagsStatusModel::class.java)
                if (model.status == SUCCESS_STATUS_EDGE) {
                    if (model.data.size > 0) {
                        tagModelArray = ArrayList()

                        model.data.forEach {
                            if (!it.tag1) {
                                tagModelArray.add(TagModel("1", false, it.tag1Name))
                            }
                            if (!it.tag2) {
                                tagModelArray.add(TagModel("1", false, it.tag2Name))
                            }
                        }
                        /*Got Those Tag which are disconnected*/
                        showStoredAlert()


                        trailerList.clear()
                        trailerList.addAll(model.data)
                        showHideProgressBar(false)
                        adapter.notifyDataSetChanged()
                    } else {
                        showHideProgressBar(false)
                        /*Need to show no data found layout*/
                    }
                } else {
                    /*Need to show no data found layout*/
                    showHideProgressBar(false)
                }


            }
        }
    }

    private fun showStoredAlert() {
        try {
            if (requireActivity().isFinishing) {
                return
            }
            try {
                if (stoedAlertDialog != null) {
                    stoedAlertDialog.dismiss()
                }
            } catch (e: java.lang.Exception) {

            }

            stoedAlertDialog = Dialog(requireActivity())
            stoedAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            stoedAlertDialog.setCancelable(true)
            stoedAlertDialog.setContentView(R.layout.stored_alert_layout)
            stoedAlertDialog.show()
            stoedAlertDialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val saveStoredAlert = stoedAlertDialog.findViewById(R.id.saveStoredAlert) as Button
            val rvAlertaLayout = stoedAlertDialog.findViewById(R.id.rvAlertaLayout) as RecyclerView
            rvAlertaLayout.layoutManager = LinearLayoutManager(context!!)
            storedAlertAdapter = StoredAlertAdapter(context!!, tagModelArray)

            rvAlertaLayout.adapter = storedAlertAdapter
            saveStoredAlert.setOnClickListener {
                stoedAlertDialog.dismiss()
            }
        } catch (e: Exception) {

        }
    }


    override fun onResume() {
        super.onResume()
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
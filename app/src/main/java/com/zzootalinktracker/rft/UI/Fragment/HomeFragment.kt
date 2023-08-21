package com.zzootalinktracker.rft.UI.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.AddSpace10XBTDataModel
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerTagModel
import com.zzootalinktracker.rft.UI.Fragment.Adapter.ChillerAdapter
import com.zzootalinktracker.rft.UI.Fragment.Adapter.StoredAlertAdapter
import com.zzootalinktracker.rft.UI.Fragment.Adapter.StoredMissingTagsAdapter
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import com.zzootalinktracker.rft.Utils.SUCCESS_STATUS_EDGE
import com.zzootalinktracker.rft.Utils.getCurrentDateTime24Hour
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment() : Fragment(), View.OnClickListener,
    StoredMissingTagsAdapter.OnRadioButtonClickListener {


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
    private lateinit var tagModelArray: ArrayList<TrailerTagModel>
    private lateinit var stoedAlertDialog: Dialog
    private var isStoredOrMissing = ""
    private var macAddress = ""



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
            rvChiller.layoutManager = LinearLayoutManager(context!!)
            showHideProgressBar(true)
            tagModelArray = ArrayList()
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
                        if (tagModelArray.size == 0) {
                            model.data.forEach {

                                var list = ArrayList<TrailerTagModel.Tags>()
                                if (!it.tag1) {
                                    list.add(
                                        TrailerTagModel.Tags(
                                            "1",
                                            null,
                                            it.tag1Name, it.tag1Imei
                                        )
                                    )
                                }
                                if (!it.tag2) {
                                    list.add(
                                        TrailerTagModel.Tags(
                                            "1",
                                            null,
                                            it.tag2Name, it.tag2Imei
                                        )
                                    )
                                }
                                if (list.size > 0) {
                                    if (it.imei == "2302210003E9") {
                                        val model =
                                            TrailerTagModel(
                                                it.trailerId,
                                                it.trailerName,
                                                it.imei,
                                                list
                                            )
                                        tagModelArray.add(model)
                                    }

                                }
                                macAddress = it.imei
                             if (it.tag2IsMissingOrStored == "MISSING"){
                                 sendSMS()
                             }
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

private fun sendSMS() {
    val phoneNumber = "8360082296" // Replace with the desired phone number
    val message = "Hello, Your tag is missing." // Replace with your message content

    val smsIntent = Intent(Intent.ACTION_VIEW)
    smsIntent.data = Uri.parse("sms:$phoneNumber")
    smsIntent.putExtra("sms_body", message)

    try {
        startActivity(smsIntent)
    } catch (ex: Exception) {
        Toast.makeText(context!!, "Failed to open SMS app", Toast.LENGTH_SHORT).show()
        ex.printStackTrace()
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
                    return
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
            storedAlertAdapter = StoredAlertAdapter(context!!, tagModelArray, this@HomeFragment)

            rvAlertaLayout.adapter = storedAlertAdapter
            saveStoredAlert.setOnClickListener {
                stoedAlertDialog.dismiss()
                /*addSpace10XData()*/
              //  sendStoredData()


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
        when (v) {

        }
    }

    private fun sendStoredData() {

        if (tagModelArray.size > 0) {
            val currentTime = Calendar.getInstance().time
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateTime = dateTimeFormat.format(currentTime)

            // Convert to UTC-8
            val utcMinus8Format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            utcMinus8Format.timeZone = TimeZone.getTimeZone("GMT-8")
            val utcMinus8DateTime = utcMinus8Format.format(currentTime)

            // Convert to Unix timestamp in UTC-8
            val utcMinus8UnixTimestamp = utcMinus8Format.parse(utcMinus8DateTime)?.time ?: -1

            // Convert Unix timestamp to hexadecimal
            val hexadecimalTimestamp = if (utcMinus8UnixTimestamp != -1L) {
                java.lang.Long.toHexString(utcMinus8UnixTimestamp / 1000).toUpperCase()
            } else {
                "N/A"
            }
            val list = ArrayList<AddSpace10XBTDataModel.Data>()
            var trailerModel = tagModelArray[tagModelArray.size - 1]
            trailerModel.taglist.forEach {
                val updateModel = AddSpace10XBTDataModel.Data(
                    hexadecimalTimestamp, it.tagImei, it.status!!
                )
                list.add(updateModel)
            }

            var model = AddSpace10XBTDataModel(trailerModel.imei, list)
            try {
                ApiInterface.createForRFT().addSpace10XBTData(
                    "bt_" + "$" + "a*lGdNlIfzcY8h*KidxAoBff*LepB4onmJo1", model
                )
                    .enqueue(object : Callback<AddSpace10XBTDataModel> {
                        override fun onResponse(
                            call: Call<AddSpace10XBTDataModel>,
                            response: Response<AddSpace10XBTDataModel>
                        ) {
                            if (response.isSuccessful) {
                                tagModelArray.removeAt(tagModelArray.size - 1)
                                if (tagModelArray.size > 0) {
                                    sendStoredData()
                                }else{
                                 /*Hide the progress bar here / dismis dialog here*/
                                }
                            } else {

                            }
                        }

                        override fun onFailure(
                            call: Call<AddSpace10XBTDataModel>,
                            t: Throwable
                        ) {
//                            t.message.toString()
                        }

                    })
            } catch (e: Exception) {

            }
        }

    }

    override fun onRadioButtonClicked(text: String, model: TrailerTagModel.Tags, position: Int) {
        for (i in tagModelArray.indices) {
            model.status = text
            if (tagModelArray[i].taglist.size >= position) {
                if (tagModelArray[i].taglist[position].tagImei == model.tagImei) {
                    tagModelArray[i].taglist[position] = model
                    isStoredOrMissing = text
                }
            }
        }
        storedAlertAdapter.notifyDataSetChanged()
    }


}
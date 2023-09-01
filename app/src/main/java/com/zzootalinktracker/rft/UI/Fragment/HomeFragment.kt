package com.zzootalinktracker.rft.UI.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
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
import com.zzootalinktracker.rft.Utils.*
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
    private lateinit var dataNotFoundhome: LinearLayout
    private lateinit var noServerFoundhome: LinearLayout
    private lateinit var viewLayout: View
    private lateinit var tvLastRefreshed: TextView
    private lateinit var tagModelArray: ArrayList<TrailerTagModel>
    private lateinit var stoedAlertDialog: Dialog
    private lateinit var btnTryAgainNoData: Button
    private var isStoredOrMissing = ""
    private var macAddress = ""
    private val version = Build.VERSION.SDK_INT

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            noServerFoundhome = viewLayout.findViewById(R.id.noServerFoundhome)
            tvDriverName = viewLayout.findViewById(R.id.tvDriverName)
            tvLastRefreshed = viewLayout.findViewById(R.id.tvLastRefreshed)
            dataNotFoundhome = viewLayout.findViewById(R.id.dataNotFoundhome)
            btnTryAgainNoData = viewLayout.findViewById(R.id.btnTryAgainNoData)
            btnTryAgainNoData.visibility = View.GONE
            rvChiller.layoutManager = LinearLayoutManager(context!!)
            updateUI(PROGRESS_BAR)
            var driverName = sessionManager.getDriverName()
            if (driverName != "") {
                tvDriverName.text = "Hey, $driverName"
            }
            tagModelArray = ArrayList()
            setAdapter()
        } catch (e: Exception) {
            Log.e("HomeFragment", e.message.toString())
        }
    }

    private fun updateUI(type: Int) {
        when (type) {
            ADAPTER_LAYOUT -> {
                progressBar.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                dataNotFoundhome.visibility = View.GONE
                noServerFoundhome.visibility = View.GONE
            }
            NO_DATA_FOUND -> {
                progressBar.visibility = View.GONE
                scrollView.visibility = View.GONE
                noServerFoundhome.visibility = View.GONE
                dataNotFoundhome.visibility = View.VISIBLE
            }
            PROGRESS_BAR -> {
                progressBar.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
                noServerFoundhome.visibility = View.GONE
                dataNotFoundhome.visibility = View.GONE
            }
            NO_SERVER -> {
                progressBar.visibility = View.GONE
                scrollView.visibility = View.GONE
                noServerFoundhome.visibility = View.VISIBLE
                dataNotFoundhome.visibility = View.GONE
            }
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
                        trailerList.clear()
                        trailerList.addAll(model.data)
                        adapter.notifyDataSetChanged()
                        updateUI(ADAPTER_LAYOUT)
                        tagModelArray.clear()
                        model.data.forEach {

                            try {
                                if (stoedAlertDialog != null) {
                                    if (stoedAlertDialog.isShowing) {
                                        return
                                    }

                                }
                            } catch (e: Exception) {

                            }
                            var list = ArrayList<TrailerTagModel.Tags>()


                            if (!it.tag1 && it.tag1IsMissingOrStored == null) {
                                list.add(
                                    TrailerTagModel.Tags(
                                        "1", null, it.tag1Name, it.tag1Imei
                                    )
                                )
                            }
                            if (!it.tag2 && it.tag2IsMissingOrStored == null) {
                                list.add(
                                    TrailerTagModel.Tags(
                                        "1", null, it.tag2Name, it.tag2Imei
                                    )
                                )
                            }
                            if (list.size > 0) {
                                if (it.imei == "2302210003E9") {
                                    val model = TrailerTagModel(
                                        it.trailerId, it.trailerName, it.imei, list
                                    )
                                    tagModelArray.add(model)
                                    showStoredAlert()
                                }

                            }

                        }
                    } else {
                        updateUI(NO_DATA_FOUND)
                    }
                }
            }
            if (intent.action == "failure") {
                updateUI(NO_SERVER)
            }
            if (intent.action == "unsucesssfull") {
                updateUI(NO_DATA_FOUND)
            }
        }
    }

    private fun showStoredAlert() {
        try {
            if (requireActivity().isFinishing) {
                return
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

                var status = true
                tagModelArray.forEach {
                    it.taglist.forEach {
                        if (it.status == null) {
                            status = false
                        }
                    }
                }
                if (status) {
                    sendStoredData()
                    stoedAlertDialog.dismiss()
                } else {
                    Toast.makeText(context, "Please Select the status of tags", Toast.LENGTH_LONG)
                        .show()
                }
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
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(trailerStatusReceiver, IntentFilter("unsucesssfull"))
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(trailerStatusReceiver, IntentFilter("failure"))
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
            val utcDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            utcDateTimeFormat.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC time zone
            val utcDateTime = utcDateTimeFormat.format(currentTime)
            val utcUnixTimestamp = utcDateTimeFormat.parse(utcDateTime)?.time ?: -1
            // Convert Unix timestamp to hexadecimal
            val hexadecimalTimestamp = if (utcUnixTimestamp != -1L) {
                java.lang.Long.toHexString(utcUnixTimestamp / 1000).toUpperCase()
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
                ).enqueue(object : Callback<AddSpace10XBTDataModel> {
                    override fun onResponse(
                        call: Call<AddSpace10XBTDataModel>,
                        response: Response<AddSpace10XBTDataModel>
                    ) {
                        if (response.isSuccessful) {
                            tagModelArray.removeAt(tagModelArray.size - 1)
                            if (tagModelArray.size > 0) {
                                sendStoredData()
                            } else {
                                Toast.makeText(
                                    context, "Status successfully Updated", Toast.LENGTH_LONG
                                ).show()
                                /*Hide the progress bar here / dismis dialog here*/
                                progressBar.visibility = View.GONE
                            }
                        } else {
                            addFlurryErrorEvents(
                                "homeFragment",
                                "RFT/AddSpace10XBTData",
                                sessionManager.getIMEI(),
                                version.toString(),
                                response.message(),
                                "apiUnsuccess"
                            )

                        }
                    }

                    override fun onFailure(
                        call: Call<AddSpace10XBTDataModel>, t: Throwable
                    ) {
                        addFlurryErrorEvents(
                            "homeFragment",
                            "RFT/AddSpace10XBTData",
                            sessionManager.getIMEI(),
                            version.toString(),
                            t.message.toString(),
                            "apiFailure"
                        )
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


    private fun getTrailerTagsStatus() {
        try {
            if (isOnline(context!!)) {
                try {
                    ApiInterface.createForRFT().getTrailerTagsStatus(
                        "\$2y\$10\$4.wpOs8L6jrJTzgbQKvDwexF8FNvwX/FRrFEsvM/avo.ah8gGa1iC",
                        sessionManager.getRftDriverId()
                    ).enqueue(object : Callback<GetTrailerTagsStatusModel> {
                        override fun onResponse(
                            call: Call<GetTrailerTagsStatusModel>,
                            response: Response<GetTrailerTagsStatusModel>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null && responseBody.status == SUCCESS_STATUS_EDGE) {
                                    val jsonString = Gson().toJson(responseBody)


                                } else {

                                }
                            } else {

                            }
                        }

                        override fun onFailure(
                            call: Call<GetTrailerTagsStatusModel>, t: Throwable
                        ) {

                            addFlurryErrorEvents(
                                "trailerTagService",
                                "trailerTagServiceApi",
                                sessionManager.getIMEI(),
                                version.toString(),
                                t.message.toString(),
                                "apiFailure"
                            )
                        }


                    })

                } catch (e: Exception) {
                }
            } else {

            }

        } catch (e: Exception) {

        }
    }


}
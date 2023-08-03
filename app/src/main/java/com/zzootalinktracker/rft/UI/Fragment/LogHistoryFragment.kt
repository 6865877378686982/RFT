package com.zzootalinktracker.rft.UI.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTagsStatusHistoryModel
import com.zzootalinktracker.rft.Utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class LogHistoryFragment() : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recycler_view: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var spinner: Spinner
    private lateinit var noInternetLayout: RelativeLayout
    private lateinit var progressBarLayout: RelativeLayout
    private lateinit var noDataLayout: RelativeLayout
    private lateinit var mainLayoutLog: RelativeLayout
    private lateinit var noServerFound: RelativeLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    val dropdownFilterList = arrayOf(
        "Today", "7 Days", "30 Days", "Custom"
    )
    private lateinit var viewLayout: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLayout = inflater.inflate(R.layout.fragment_log_history, container, false)
        initView()


        return viewLayout
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initView() {
        recycler_view = viewLayout.findViewById(R.id.recycler_view)
        spinner = viewLayout.findViewById(R.id.spinner)
        swipeRefreshLayout = viewLayout.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        noInternetLayout = viewLayout.findViewById(R.id.noInternetLayout)
        noDataLayout = viewLayout.findViewById(R.id.noDataLayout)
        noServerFound = viewLayout.findViewById(R.id.noServerFound)
        mainLayoutLog = viewLayout.findViewById(R.id.mainLayoutLog)
        progressBarLayout = viewLayout.findViewById(R.id.progressBarLayout)
        sessionManager = SessionManager(context!!)
        recycler_view.layoutManager = LinearLayoutManager(context!!)
        val adapter =
            ArrayAdapter(context!!, R.layout.spinner, dropdownFilterList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text = p0!!.getItemAtPosition(p2).toString()
                when (text) {
                    "Custom" -> {
                        setupRangePickerDialog()
                    }
                    "7 Days" -> {
                        getTagsStatusHistory(getCurrentDateForEdge(), getCalculatedDate(-7))
                    }
                    "30 Days" -> {
                        getTagsStatusHistory(getCurrentDateForEdge(), getCalculatedDate(-30))
                    }
                    "Today" -> {
                        getTagsStatusHistory(getCurrentDateForEdge(), getCurrentDateForEdge())
                    }
                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {


            }


        }

        getTagsStatusHistory(getCurrentDateForEdge(), getCurrentDateForEdge())
    }

    private fun updateUI(mode: Int) {
        when (mode) {
            NO_INTERNET -> {
                noInternetLayout.visibility = View.VISIBLE
                progressBarLayout.visibility = View.GONE
                mainLayoutLog.visibility = View.GONE
                noServerFound.visibility = View.GONE
                noDataLayout.visibility = View.GONE
            }
            NO_SERVER -> {
                noInternetLayout.visibility = View.GONE
                progressBarLayout.visibility = View.GONE
                mainLayoutLog.visibility = View.GONE
                noServerFound.visibility = View.VISIBLE
                noDataLayout.visibility = View.GONE
            }
            NO_DATA_FOUND -> {
                noInternetLayout.visibility = View.GONE
                progressBarLayout.visibility = View.GONE
                mainLayoutLog.visibility = View.GONE
                noServerFound.visibility = View.GONE
                noDataLayout.visibility = View.VISIBLE
            }
            PROGRESS_BAR -> {
                noInternetLayout.visibility = View.GONE
                progressBarLayout.visibility = View.VISIBLE
                mainLayoutLog.visibility = View.GONE
                noServerFound.visibility = View.GONE
                noDataLayout.visibility = View.GONE
            }
            ADAPTER_LAYOUT -> {
                noInternetLayout.visibility = View.GONE
                progressBarLayout.visibility = View.GONE
                mainLayoutLog.visibility = View.VISIBLE
                noServerFound.visibility = View.GONE
                noDataLayout.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupRangePickerDialog() {
        try {
            val builder: MaterialDatePicker.Builder<*> =
                MaterialDatePicker.Builder.dateRangePicker()
            val constraintsBuilder =
                CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())
            try {
                builder.setCalendarConstraints(constraintsBuilder.build())
                val picker: MaterialDatePicker<*> = builder.build()

                getDateRange(picker)
                fragmentManager?.let { picker.show(it, picker.toString()) }


            } catch (e: IllegalArgumentException) {
            }

        } catch (e: Exception) {

        }

    }

    private fun getDateRange(materialCalendarPicker: MaterialDatePicker<out Any>) {
        materialCalendarPicker.addOnPositiveButtonClickListener {
            val selection = it as androidx.core.util.Pair<*, *>
            val startDate = selection.first as Long
            val endDate = selection.second as Long
            val startDateString = convertMillisToDate(startDate)
            val endDateString = convertMillisToDate(endDate)
            getTagsStatusHistory(startDateString, endDateString)
            Log.e("Date123", "Date123")

        }


    }

    private fun convertMillisToDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    private fun getTagsStatusHistory(startDate: String, endDate: String) {
            if (isOnline(context!!)) {
                updateUI(PROGRESS_BAR)
                try {
                    ApiInterface.createForRFT().getTagsStatusHistory(
                        "\$2y\$10$" + "UxX6IwSI56UNrQGDNDOL/e2MM6fUVUU9LTx.8lnIQEDGFdRt.ZfUu",
                        "005087", startDate, endDate
                    ).enqueue(object : Callback<GetTagsStatusHistoryModel> {
                        override fun onResponse(
                            call: Call<GetTagsStatusHistoryModel>,
                            response: Response<GetTagsStatusHistoryModel>
                        ) {
                            if (response.isSuccessful) {

                                if (response.body()!!.status == SUCCESS_STATUS_EDGE) {
                                    if (response.body()!!.data != null) {
                                        updateUI(ADAPTER_LAYOUT)
                                    } else {
                                        updateUI(NO_DATA_FOUND)
                                    }

                                } else {
                                    updateUI(NO_DATA_FOUND)
                                }
                            } else {
                                updateUI(NO_SERVER)
                            }

                        }

                        override fun onFailure(
                            call: Call<GetTagsStatusHistoryModel>,
                            t: Throwable
                        ) {
                            updateUI(NO_SERVER)
                        }


                    })

                } catch (e: Exception) {

                }
            } else {
                updateUI(NO_INTERNET)
            }
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = false
        getTagsStatusHistory(getCurrentDateForEdge(), getCurrentDateForEdge())
    }

}
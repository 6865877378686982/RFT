package com.zzootalinktracker.rft.UI.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var text_view: TextView
    private lateinit var noInternetLayout: RelativeLayout
    private lateinit var progressBarLog: ProgressBar
    private lateinit var noDataLayout: RelativeLayout
    private lateinit var mainLayoutLog: RelativeLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    val dropdownFilterList = arrayOf(
        "Today", "7 Days", "30 Days", "Custom"
    )

    /* private lateinit var customScrollbar: ImageView*/
    private lateinit var viewLayout: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLayout = inflater.inflate(R.layout.fragment_log_history, container, false)
        recycler_view = viewLayout.findViewById(R.id.recycler_view)
        spinner = viewLayout.findViewById(R.id.spinner)
        text_view = viewLayout.findViewById(R.id.text_view)
        swipeRefreshLayout = viewLayout.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {

        }
        noInternetLayout = viewLayout.findViewById(R.id.noInternetLayout)
        noDataLayout = viewLayout.findViewById(R.id.noDataLayout)
        mainLayoutLog = viewLayout.findViewById(R.id.mainLayoutLog)
        progressBarLog = viewLayout.findViewById(R.id.progressBarLog)
        progressBarLog.visibility = View.GONE
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


        return viewLayout
    }

    override fun onResume() {
        super.onResume()
        spinner.setSelection(0)
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
        try {
            if (isOnline(context!!)) {

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
                                progressBarLog.visibility = View.GONE
                                if (response.body()!!.status != SUCCESS_STATUS_EDGE) {
                                    if (response.body()!!.data != null) {

                                    } else {
                                        progressBarLog.visibility = View.VISIBLE
                                    }

                                } else {
                                    /* noDataLayout.visibility = View.VISIBLE
                                     mainLayoutLog.visibility = View.GONE*/
                                }
                            } else {
                                /* noDataLayout.visibility = View.VISIBLE
                                 mainLayoutLog.visibility = View.GONE*/
                                progressBarLog.visibility = View.VISIBLE
                            }

                        }

                        override fun onFailure(
                            call: Call<GetTagsStatusHistoryModel>,
                            t: Throwable
                        ) {
                            /*  noDataLayout.visibility = View.VISIBLE
                              mainLayoutLog.visibility = View.GONE*/
                        }


                    })

                } catch (e: Exception) {

                }
            } else {
                /*  val intent = Intent(context, NoInternetScreen::class.java)
                  startActivity(intent)*/
                /*   noInternetLayout.visibility = View.VISIBLE
                   mainLayoutLog.visibility = View.GONE*/
            }

        } catch (e: Exception) {

        }


    }

    override fun onRefresh() {
        text_view.text = "EHello"
        swipeRefreshLayout.isRefreshing = true
    }

}
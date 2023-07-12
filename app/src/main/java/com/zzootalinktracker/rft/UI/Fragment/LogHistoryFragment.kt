package com.zzootalinktracker.rft.UI.Fragment

import TrailerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerModel
import java.text.SimpleDateFormat
import java.util.*


class LogHistoryFragment : Fragment() {
    private lateinit var recycler_view: RecyclerView
   /* private lateinit var customScrollbar: ImageView*/
    private lateinit var viewLayout: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLayout = inflater.inflate(R.layout.fragment_log_history, container, false)
        recycler_view = viewLayout.findViewById(R.id.recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(context!!)

        val currentDateTime = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = dateFormat.format(currentDateTime)
        val dateTimeString: String = formattedDateTime
        val data = ArrayList<TrailerModel>()


        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 are Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg",
                true
            )
        )

        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 is Disconnected",
                "Please retrieve Chiller Pad #1 before continuing with your leg",
                false
            )
        )

        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg", true
            )
        )

        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg", true
            )
        )

        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg", false
            )
        )
        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg", false
            )
        )
        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg", true
            )
        )
        data.add(
            TrailerModel(
                dateTimeString,
                "Chiller 1 & Chiller 2 Connected",
                "Please retrieve Chiller Pad #1 before continuing with your leg", true
            )
        )
        val adapter = TrailerAdapter(data)
        recycler_view.adapter = adapter


        return viewLayout
    }

}
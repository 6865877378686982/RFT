package com.zzootalinktracker.rft.UI.Fragment.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerTagModel

class StoredAlertAdapter(
    context: Context,
    var list: ArrayList<TrailerTagModel>,
    var onBoxSelected: StoredMissingTagsAdapter.OnRadioButtonClickListener
) : RecyclerView.Adapter<StoredAlertAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvImei = itemView.findViewById<TextView>(R.id.tvImei)

        fun bindView(
            model: TrailerTagModel,
            position: Int,
            onBoxSelected: StoredMissingTagsAdapter.OnRadioButtonClickListener
        ) {

            val imei = model.imei
            tvImei.text = imei

            val innerRecyclerView = itemView.findViewById<RecyclerView>(R.id.rvTags)
            val innerAdapter = StoredMissingTagsAdapter(model.taglist, onBoxSelected)
            innerRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            innerRecyclerView.adapter = innerAdapter


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stored_layout, parent, false)

        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bindView(item, position, onBoxSelected)
    }

    override fun getItemCount(): Int {
        return list.size
    }








}

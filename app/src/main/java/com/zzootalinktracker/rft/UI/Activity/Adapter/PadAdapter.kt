package com.zzootalinktracker.rft.UI.Activity.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.ChillerModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel

class PadAdapter(context: Context, private val mlist: ArrayList<GetTrailerTagsStatusModel.Data>) :
    RecyclerView.Adapter<PadAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: GetTrailerTagsStatusModel.Data) {
            val tvPadName = itemView.findViewById<TextView>(R.id.tvPadName)
            val tvPadStatus = itemView.findViewById<TextView>(R.id.tvPadStatus)

            tvPadName.text = item.tag1Name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.pads_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mlist[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}

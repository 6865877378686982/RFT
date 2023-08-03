package com.zzootalinktracker.rft.UI.Fragment.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Fragment.Model.TagModel

class StoredAlertAdapter(context: Context, var list: ArrayList<TagModel>) :
    RecyclerView.Adapter<StoredAlertAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTagName = itemView.findViewById<TextView>(R.id.tvTagName)
        fun bindView(model: TagModel) {
            tvTagName.text = model.tagName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stored_alert_item_layout, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

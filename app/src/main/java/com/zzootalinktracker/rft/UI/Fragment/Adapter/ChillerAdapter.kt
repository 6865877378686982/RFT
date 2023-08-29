package com.zzootalinktracker.rft.UI.Fragment.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import de.hdodenhof.circleimageview.CircleImageView

class ChillerAdapter(
    var context: Context, var mlist: ArrayList<GetTrailerTagsStatusModel.Data>
) : RecyclerView.Adapter<ChillerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTrailerName: TextView = itemView.findViewById(R.id.tvChillerName)
        private val tvtag1: TextView = itemView.findViewById(R.id.tvtag1)
        private val tvtag1Status: TextView = itemView.findViewById(R.id.tvtag1Status)
        private val tvtag2: TextView = itemView.findViewById(R.id.tvtag2)
        private val tvtag2Status: TextView = itemView.findViewById(R.id.tvtag2Status)
        private val ivChillerPadStatus: CircleImageView =
            itemView.findViewById(R.id.ivChillerPadStatus)

        fun bind(item: GetTrailerTagsStatusModel.Data) {
            tvTrailerName.text = item.trailerName
            tvtag1.text = item.tag1Name
            tvtag2.text = item.tag2Name
            val tag1 = item.tag1
            if (tag1) {
                tvtag1Status.text = "Connected"
                tvtag1Status.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                if (item.tag1IsMissingOrStored == null) {
                    tvtag1Status.text = "Not Connected"
                    tvtag1Status.setTextColor(ContextCompat.getColor(context, R.color.red_rft))
                } else {
                    if (item.tag1IsMissingOrStored == "STORED") {
                        tvtag1Status.setTextColor(ContextCompat.getColor(context, R.color.amber))
                        tvtag1Status.text = "STORED"
                    } else {
                        tvtag1Status.setTextColor(ContextCompat.getColor(context, R.color.red))
                        tvtag1Status.text = "MISSING"
                    }
                }

            }

            val tag2 = item.tag2
            if (tag2) {
                tvtag2Status.text = "Connected"
                tvtag2Status.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                if (item.tag2IsMissingOrStored == null) {
                    tvtag2Status.text = "Not Connected"
                    tvtag2Status.setTextColor(ContextCompat.getColor(context, R.color.red_rft))
                } else {
                    if (item.tag2IsMissingOrStored == "STORED") {
                        tvtag2Status.setTextColor(ContextCompat.getColor(context, R.color.amber))
                        tvtag2Status.text = "STORED"
                    } else {
                        tvtag2Status.setTextColor(ContextCompat.getColor(context, R.color.red))
                        tvtag2Status.text = "MISSING"
                    }
                }
            }

            if (tag1 && tag2) {
                ivChillerPadStatus.setImageResource(R.drawable.color_green)
            } else {
                if (item.tag2IsMissingOrStored != null && item.tag1IsMissingOrStored != null) {
                    if (item.tag2IsMissingOrStored == "STORED" && item.tag1IsMissingOrStored == "STORED") {
                        ivChillerPadStatus.setImageResource(R.drawable.amber_color)
                    } else {
                        ivChillerPadStatus.setImageResource(R.drawable.solid_red)
                    }
                } else {
                    ivChillerPadStatus.setImageResource(R.drawable.solid_red)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.chiller_layout, parent, false)
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

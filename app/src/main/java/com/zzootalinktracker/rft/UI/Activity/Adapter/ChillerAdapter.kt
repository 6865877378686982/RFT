package com.zzootalinktracker.rft.UI.Activity.Adapter

import android.animation.ObjectAnimator
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel

import de.hdodenhof.circleimageview.CircleImageView

class ChillerAdapter(
    var context: Context,
    var mlist: ArrayList<GetTrailerTagsStatusModel.Data>
) :
    RecyclerView.Adapter<ChillerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTrailerName: TextView = itemView.findViewById(R.id.tvChillerName)
        private val tvtag1: TextView = itemView.findViewById(R.id.tvtag1)
        private val tvtag1Status: TextView = itemView.findViewById(R.id.tvtag1Status)
        private val tvtag2: TextView = itemView.findViewById(R.id.tvtag2)
        private val tvtag2Status: TextView = itemView.findViewById(R.id.tvtag2Status)
        private val dropdownChecklist: ImageView = itemView.findViewById(R.id.dropdown_checklist)
        private val tagLayout: LinearLayout = itemView.findViewById(R.id.tagLayout)
        private val ivChillerPadStatus: CircleImageView =
            itemView.findViewById(R.id.ivChillerPadStatus)
        private var rotationAngle = 0f

        /*  lateinit var adapter: PadAdapter*/
        fun bind(item: GetTrailerTagsStatusModel.Data) {
            tvTrailerName.text = item.trailerName
            tvtag1.text = item.tag1Name
            tvtag2.text = item.tag2Name
            val tag1 = item.tag1
            if (tag1) {
                tvtag1Status.text = "Connected"
            } else {
                tvtag1Status.text = "Not Connected"
            }

            val tag2 = item.tag2
            if (tag2) {
                tvtag2Status.text = "Connected"
            } else {
                tvtag2Status.text = "Not Connected"
            }

            if (tag1 && tag2) {
                ivChillerPadStatus.setImageResource((R.color.green))
            } else {
                ivChillerPadStatus.setImageResource((R.color.red))
            }


            /*tagLayout.visibility = View.GONE*/
            /*rvPads.layoutManager = LinearLayoutManager(context)
            adapter = PadAdapter(context!!, mlist)
            rvPads.adapter = adapter*/



          /*  dropdownChecklist.setOnClickListener {
                openClosedRV()
            }
            itemView.setOnClickListener {
                openClosedRV()
            }*/


        }

        private fun openClosedRV() {
            try {
                val anim = ObjectAnimator.ofFloat(
                    dropdownChecklist,
                    "rotation",
                    rotationAngle,
                    rotationAngle + 180
                )
                anim.duration = 200
                anim.start()
                rotationAngle += 180f
                rotationAngle %= 360


                //dropdown_checklist.setColorFilter(com.sygic.example.hello3dwiw.R.color.emoji_color)
                if (tagLayout.visibility == View.VISIBLE) {
                    tagLayout.visibility = View.GONE


                } else {

                    tagLayout.visibility = View.VISIBLE
                }
            } catch (e: java.lang.Exception) {

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chiller_layout, parent, false)
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

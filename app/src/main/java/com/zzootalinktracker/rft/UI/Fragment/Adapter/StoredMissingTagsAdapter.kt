package com.zzootalinktracker.rft.UI.Fragment.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerTagModel

class StoredMissingTagsAdapter(
    var list: ArrayList<TrailerTagModel.Tags>,
    var onBoxSelected: OnRadioButtonClickListener
) :
    RecyclerView.Adapter<StoredMissingTagsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTagName = itemView.findViewById<TextView>(R.id.tvTagName)
        var tvImei = itemView.findViewById<TextView>(R.id.tvImei)
        var radioGroup = itemView.findViewById<RadioGroup>(R.id.radioGroup)
        var btnStored = itemView.findViewById<RadioButton>(R.id.btnStored)
        var btnMissing = itemView.findViewById<RadioButton>(R.id.btnMissing)


        fun bindView(
            model: TrailerTagModel.Tags,
            position: Int,
            onBoxSelected: OnRadioButtonClickListener
        ) {
               val tagImei = model.tagImei
               tvTagName.text = tagImei

            if (model.status != null) {
                if(model.status=="MISSING"){
                    btnMissing.isChecked = true
                }else{
                    btnStored.isChecked = true
                }
            }

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val radioButton = group.findViewById<RadioButton>(checkedId)
                val radioButtonText = radioButton.text
                onBoxSelected.onRadioButtonClicked(radioButtonText.toString(), model, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stored_missing_tags_item_layout, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bindView(item, position, onBoxSelected)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnRadioButtonClickListener {
        fun onRadioButtonClicked(text: String, model: TrailerTagModel.Tags, position: Int)
    }

}
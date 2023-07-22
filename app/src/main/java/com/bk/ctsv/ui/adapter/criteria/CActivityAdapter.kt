package com.bk.ctsv.ui.adapter.criteria

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.databinding.ListItemCActivityBinding
import com.bk.ctsv.models.entity.Activity

class CActivityAdapter(
    var activities: List<Activity>
): RecyclerView.Adapter<CActivityAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemCActivityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(activity: Activity){
            binding.apply {
                activityNameText.text = activity.name
                val arr = activity.criteriaLst?.map {
                    " - (${it.maxPoint}) ${it.name}"
                }
                criteriaText.text = arr?.joinToString(separator = "\n")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCActivityBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(activity = activities[position])
    }

    override fun getItemCount(): Int {
        return activities.size
    }
}
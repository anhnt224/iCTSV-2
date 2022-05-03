package com.bk.ctsv.ui.adapter.running

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemRunningDataBinding
import com.bk.ctsv.models.req.RunningDataRequest

class RunningDataAdapter(var runningDataList: List<RunningDataRequest>): RecyclerView.Adapter<RunningDataAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemRunningDataBinding): RecyclerView.ViewHolder(binding.root){
        fun bindView(runningData: RunningDataRequest){
            binding.runningData = runningData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ListItemRunningDataBinding>(inflater, R.layout.list_item_running_data, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(runningData = runningDataList[position])
    }

    override fun getItemCount(): Int {
        return runningDataList.size
    }
}
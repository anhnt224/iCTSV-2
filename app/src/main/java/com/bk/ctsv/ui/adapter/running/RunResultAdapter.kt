package com.bk.ctsv.ui.adapter.running

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemRunResultBinding
import com.bk.ctsv.models.entity.run.RunResult

class RunResultAdapter(var runResults: List<RunResult>): RecyclerView.Adapter<RunResultAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemRunResultBinding): RecyclerView.ViewHolder(binding.root){
        fun bindView(runResult: RunResult){
            binding.runResult = runResult
            if (runResult.getPaceToSetResult()){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(DataBindingUtil.inflate(inflater, R.layout.list_item_run_result, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(runResult = runResults[position])
    }

    override fun getItemCount(): Int {
        return runResults.size
    }
}
package com.bk.ctsv.ui.adapter.running

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemRunResultBinding
import com.bk.ctsv.models.entity.run.RunResult

class RunResultAdapter(var runResults: List<RunResult>): RecyclerView.Adapter<RunResultAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemRunResultBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ResourceAsColor")
        fun bindView(runResult: RunResult){
            binding.runResult = runResult
            if (runResult.getPaceToSetResult()){
                binding.viewRunResult.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.colorRunDashBoardPass)
                )
            }else{
                binding.viewRunResult.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.colorRunDashBoardFail)
                )
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
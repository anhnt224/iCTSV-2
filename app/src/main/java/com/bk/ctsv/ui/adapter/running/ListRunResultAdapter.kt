package com.bk.ctsv.ui.adapter.running

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListRunResultItemBinding
import com.bk.ctsv.models.entity.run.RunResult

class ListRunResultAdapter(
    var runResultMap: Map<String, List<RunResult>>
): RecyclerView.Adapter<ListRunResultAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ListRunResultItemBinding
        ): RecyclerView.ViewHolder(binding.root){

        private lateinit var runResultAdapter: RunResultAdapter

        fun bindView(runResultList: List<RunResult>, position: Int){
            val key = runResultMap.keys.toList()[position]
            val runResults: List<RunResult> = runResultMap[key] ?: listOf()
            runResultAdapter = RunResultAdapter(runResults)
            binding.apply {
                textViewRunDay.text = key
                recyclerViewListRunResult.apply {
                    adapter = runResultAdapter
                    layoutManager = LinearLayoutManager(binding.root.context)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListRunResultItemBinding = inflate(inflater,
            R.layout.list_run_result_item,
            parent,
            false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = runResultMap.keys.toList()[position]
        val runResultListItem = runResultMap[key]?: listOf()
        return holder.bindView(runResultListItem, position)
    }

    override fun getItemCount(): Int {
        return runResultMap.keys.size
    }
}
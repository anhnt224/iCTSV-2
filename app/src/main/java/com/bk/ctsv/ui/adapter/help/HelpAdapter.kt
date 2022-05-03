package com.bk.ctsv.ui.adapter.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemHelpBinding
import com.bk.ctsv.models.entity.Help
import com.bk.ctsv.models.entity.Tutorial
import com.bk.ctsv.ui.adapter.OnItemClickListener

class HelpAdapter(var helps: List<Help>, val onItemClickListener: OnItemClickListener<Tutorial>): RecyclerView.Adapter<HelpAdapter.ViewHolder>(){
    class ViewHolder(val binding: ListItemHelpBinding, val onItemClickListener: OnItemClickListener<Tutorial>): RecyclerView.ViewHolder(binding.root){
        fun bindView(help: Help){
            val tutorialAdapter = TutorialAdapter(help.tutorials, onItemClickListener)
            binding.recyclerView.apply {
                adapter = tutorialAdapter
                layoutManager = LinearLayoutManager(context)
            }
            binding.textTitle.text = help.topic
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemHelpBinding>(LayoutInflater.from(parent.context), R.layout.list_item_help, parent, false)
        return ViewHolder(binding, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return helps.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(help = helps[position])
    }
}
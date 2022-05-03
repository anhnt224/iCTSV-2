package com.bk.ctsv.ui.adapter.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemTutorialBinding
import com.bk.ctsv.models.entity.Tutorial
import com.bk.ctsv.ui.adapter.OnItemClickListener

class TutorialAdapter(var tutorials: List<Tutorial>, val onItemClickListener: OnItemClickListener<Tutorial>) : RecyclerView.Adapter<TutorialAdapter.ViewHolder>(){
    class ViewHolder(val binding: ListItemTutorialBinding, val onItemClickListener: OnItemClickListener<Tutorial>): RecyclerView.ViewHolder(binding.root){
        fun bindView(tutorial: Tutorial){
            binding.root.setOnClickListener {
                onItemClickListener.onClick(tutorial)
            }
            binding.tutorial = tutorial
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemTutorialBinding>(LayoutInflater.from(parent.context), R.layout.list_item_tutorial, parent, false)
        return ViewHolder(binding, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return tutorials.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(tutorial = tutorials[position])
    }
}
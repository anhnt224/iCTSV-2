package com.bk.ctsv.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemHomeBinding
import com.bk.ctsv.models.entity.HomeItem

class HomeItem2Adapter (var items: List<HomeItem>, private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<HomeItem2Adapter.ViewHolder>(){
    class ViewHolder(val binding: ListItemHomeBinding, private val onItemClickListener: OnItemClickListener): RecyclerView.ViewHolder(binding.root){
        fun bindView(item: HomeItem, position: Int) {
            binding.apply {
                image.setImageDrawable(itemView.resources.getDrawable(item.icon))
                title.text = item.title
                root.setOnClickListener {
                    onItemClickListener.onClick2(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemHomeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_home, parent, false)
        return ViewHolder(binding, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position], position)
    }

    interface OnItemClickListener{
        fun onClick2(position: Int)
    }
}

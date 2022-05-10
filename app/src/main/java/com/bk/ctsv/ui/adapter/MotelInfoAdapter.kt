package com.bk.ctsv.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemMotelInfoBinding
import com.bk.ctsv.models.entity.Motel

class MotelInfoAdapter(
    var listMotel: List<Motel>,
    val onItemMotelInfoClickListener: OnItemMotelInfoClickListener
    ): RecyclerView.Adapter<MotelInfoAdapter.ViewHolder>() {
    class ViewHolder(
            val binding: ListItemMotelInfoBinding,
            val onItemMotelInfoClickListener: OnItemMotelInfoClickListener
        ): RecyclerView.ViewHolder(binding.root){
        fun bindView(motel: Motel){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemMotelInfoBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_item_motel_info,
            parent,
            false
        )

        return ViewHolder(binding, onItemMotelInfoClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val motel = listMotel[position]
        return holder.bindView(motel)
    }

    override fun getItemCount(): Int {
        return listMotel.size
    }

    interface OnItemMotelInfoClickListener{
        fun navigateToMotelInfoDetailFragment()
    }
}
package com.bk.ctsv.ui.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemMotelImageBinding
import com.bk.ctsv.models.entity.ImageMotel
import com.bk.ctsv.models.entity.ImageMotel2
import com.squareup.picasso.Picasso

class MotelImageAdapter(
    var motelImageList: List<ImageMotel2>
): RecyclerView.Adapter<MotelImageAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: ListItemMotelImageBinding
        ): RecyclerView.ViewHolder(binding.root){
        fun bindView(motelImage: ImageMotel2) {
            binding.apply {
                imageMotel = motelImage
                val url = "https://ctsv.hust.edu.vn/api-t/${motelImage.urlImage}"
                Picasso.get().load(url).into(image)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemMotelImageBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_item_motel_image,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val motelImage = motelImageList[position]
        return holder.bindView(motelImage)
    }

    override fun getItemCount(): Int {
        return motelImageList.size
    }
}
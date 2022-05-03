package com.bk.ctsv.ui.adapter.gift

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R
import com.bk.ctsv.databinding.ListItemGiftBinding
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.utilities.API_UPLOAD_SERVICE_BASE_URL
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class GiftAdapter(
    var gifts: List<Gift>,
    val activity: Activity,
    val onItemClickListener: OnItemClickListener,
    val userName: String,
    val token: String
) : RecyclerView.Adapter<GiftAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ListItemGiftBinding,
        val onItemClickListener: OnItemClickListener
    ): RecyclerView.ViewHolder(binding.root){
        fun bindView(gift: Gift){
            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(gift)
            }
            binding.gift = gift
            binding.apply {
                register.max = gift.quantity
                register.progress = gift.registeredQuantity
            }
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(1800)
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
            val url = "${API_UPLOAD_SERVICE_BASE_URL}ATMGift/DownloadImageGift?UserName=${userName}&TokenCode=${token}&GiftID=${gift.id}&TypeImage=1"
            Glide.with(activity)
                .load(url)
                .placeholder(shimmerDrawable)
                .error(R.drawable.ic_gift_default)
                .centerCrop()
                .into(binding.imageView)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(gift: Gift)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemGiftBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_gift,
            parent,
            false
        )
        return ViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(gifts[position])
    }

    override fun getItemCount(): Int {
        return gifts.size
    }
}
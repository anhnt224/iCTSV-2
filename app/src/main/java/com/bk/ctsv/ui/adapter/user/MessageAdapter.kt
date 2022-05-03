package com.bk.ctsv.ui.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bk.ctsv.R
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.DataBoundListAdapter
import com.bk.ctsv.databinding.ListItemMessageBinding
import com.bk.ctsv.models.entity.Message
import com.bk.ctsv.ui.diffCallbacks.MessageDiffCallback


class MessageAdapter(var appExecutors: AppExecutors,
                     private val itemClick: ((Message) -> Unit)?
) : DataBoundListAdapter<Message, ListItemMessageBinding>(
    appExecutors = appExecutors,
    diffCallback = MessageDiffCallback()
) {

    override fun createBinding(parent: ViewGroup): ListItemMessageBinding {
        val binding = DataBindingUtil.inflate<ListItemMessageBinding>(
            LayoutInflater.from(parent.context), R.layout.list_item_message, parent, false)

        binding.root.setOnClickListener {
            binding.message?.let {
                itemClick?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ListItemMessageBinding, item: Message) {
        binding.message = item
    }
}

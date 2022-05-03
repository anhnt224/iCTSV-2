package com.bk.ctsv.ui.adapter.user

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bk.ctsv.R

import com.bk.ctsv.models.entity.ScheduleStudent
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

internal class SchedulesSection(
    private val title: String, private val list: List<ScheduleStudent>,
    private val itemClick: ((ScheduleStudent) -> Unit)?
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.section_schedule_item)
        .headerResourceId(R.layout.section_schedule_header)
        .build()
) {

    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder

        val schedule = list[position]

        itemHolder.txtClassId.setText("[${schedule.classId}]")
        itemHolder.txtRoom.setText(schedule.room)
        itemHolder.txtTower.setText(schedule.tower)
        itemHolder.txtName.setText(schedule.name)
        itemHolder.txtStartTime.setText(schedule.getTime())
        itemHolder.txtType.setText(schedule.typeSubject)

      //  itemHolder.imgItem.setImageResource(contact.profileImage)
        itemHolder.rootView.setOnClickListener {
            itemClick?.invoke(schedule)
        }

    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val headerHolder = holder as HeaderViewHolder?

        headerHolder!!.tvTitle.setText(title)
    }

    internal interface ClickListener {

        fun onItemRootViewClicked(sectionTitle: String, itemAdapterPosition: Int)
    }
}
internal class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tvTitle: TextView

    init {

        tvTitle = view.findViewById(R.id.tvTitle)
    }
}
internal class ItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {

    val txtClassId: TextView
    val txtRoom: TextView
    val txtTower: TextView
    val txtName: TextView
    val txtStartTime: TextView
    val txtType: TextView
    init {
        txtClassId = rootView.findViewById(R.id.txtClassId)
        txtRoom = rootView.findViewById(R.id.txtRoom)
        txtTower = rootView.findViewById(R.id.txtTower)
        txtName = rootView.findViewById(R.id.txtName)
        txtStartTime = rootView.findViewById(R.id.txtStartTime)
        txtType = rootView.findViewById(R.id.txtType)
    }
}

package com.bk.ctsv.ui.adapter.activity

import com.bk.ctsv.models.entity.Activity

interface IActivityAdapterCallBack {
    public fun itemClick(activity: Activity)
}
package com.bk.ctsv.models.res.activity


import com.bk.ctsv.models.entity.UserActivity

import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetUserActivityByAIdRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("NumberPage")
    val numberPage: Int?,
    @SerializedName("UserActivityLst")
    val userActivityLst : List<UserActivity>?
) : CTSVCap()
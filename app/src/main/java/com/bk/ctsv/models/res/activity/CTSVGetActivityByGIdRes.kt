package com.bk.ctsv.models.res.activity


import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetActivityByGIdRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("NumberPage")
    val numberPage: Int?,
    @SerializedName("Activities")
    val activities : List<Activity>?
) : CTSVCap()
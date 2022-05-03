package com.bk.ctsv.models.res.activity


import com.bk.ctsv.models.entity.ActivityGroup
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetActivityGroupByGIdRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("ActivityGroupLst")
    val activityGroupLst : List<ActivityGroup>?
) : CTSVCap()
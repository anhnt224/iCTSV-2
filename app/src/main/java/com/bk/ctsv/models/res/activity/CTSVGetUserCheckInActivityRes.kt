package com.bk.ctsv.models.res.activity


import com.bk.ctsv.models.entity.UserCheckInActivity
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetUserCheckInActivityRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("UserCheckInActivityLst")
    val userCheckInActivityLst : List<UserCheckInActivity>?
) : CTSVCap() {


}
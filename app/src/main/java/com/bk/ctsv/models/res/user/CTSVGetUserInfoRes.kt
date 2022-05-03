package com.bk.ctsv.models.res.user



import com.bk.ctsv.models.entity.User
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetUserInfoRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("ClassDetailInfo")
    val userInfo : User?
) : CTSVCap() {


}
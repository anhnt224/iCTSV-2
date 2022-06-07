package com.bk.ctsv.models.res.user


import com.bk.ctsv.models.entity.UserGroup


import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetUserByGIdRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("NumberPage")
    val numberPage: Int?,
    @SerializedName("UserGroups")
    val userGroups : List<UserGroup>?
) : CTSVCap()
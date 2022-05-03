package com.bk.ctsv.models.res.group

import com.bk.ctsv.models.entity.SVGroup
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetSVGroupByIdRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("SVGroupInfo")
    val svGroupInfo : SVGroup?
) : CTSVCap()
package com.bk.ctsv.models.res.activity

import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetPublicActivity(
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("Activities")
    val activities : List<Activity>
) : CTSVCap()
package com.bk.ctsv.models.res.user

import com.bk.ctsv.models.entity.Message
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class CTSVGetUserMessageRes(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("UserMessageLst")
    val userMessageLst : List<Message>?
) : CTSVCap()
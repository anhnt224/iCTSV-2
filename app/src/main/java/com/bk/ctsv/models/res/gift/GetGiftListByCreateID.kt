package com.bk.ctsv.models.res.gift

import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetGiftListByCreateID (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("GiftLst")
    var gifts: List<Gift> = listOf(),
    @SerializedName("NumberPage")
    var numberPage: Int = 0
): CTSVCap()
package com.bk.ctsv.models.res.gift

import com.bk.ctsv.models.entity.gift.GiftRegister
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetGiftRegistersResp(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("UserGiftLst")
    var giftRegisters: List<GiftRegister> = listOf()
): CTSVCap()
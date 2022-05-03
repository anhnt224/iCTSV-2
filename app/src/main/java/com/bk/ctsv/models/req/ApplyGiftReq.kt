package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.gift.GiftRegister
import com.bk.ctsv.models.entity.gift.UserApprove
import com.google.gson.annotations.SerializedName

class ApplyGiftReq(
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("TokenCode")
    var token: String = "",
    @SerializedName("UserGiftInfo")
    var giftRegisterInfo: GiftRegister
)
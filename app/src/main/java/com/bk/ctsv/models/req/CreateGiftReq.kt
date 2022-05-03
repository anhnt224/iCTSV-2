package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.gift.Gift
import com.google.gson.annotations.SerializedName

class CreateGiftReq (
    @SerializedName("UserName")
    var userName: String,
    @SerializedName("TokenCode")
    var token: String,
    @SerializedName("GiftInfo")
    var gift: Gift
)
package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.gift.UserApprove
import com.google.gson.annotations.SerializedName

class ApproveUserGiftReq (
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("TokenCode")
    var token: String = "",
    @SerializedName("GiftID")
    var giftId: Int,
    @SerializedName("UserApproveLst")
    var userApproveList: List<UserApprove>
)
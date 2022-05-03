package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.UserAddress
import com.google.gson.annotations.SerializedName

class UpdateUserAddressReq (
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("TokenCode")
    var token: String = "",
    @SerializedName("StudentContactInfo")
    var userAddress: UserAddress
)
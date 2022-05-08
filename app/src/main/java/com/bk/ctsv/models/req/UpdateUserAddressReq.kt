package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.models.entity.UserAddress
import com.google.gson.annotations.SerializedName

class UpdateUserAddressReq (
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("TokenCode")
    var token: String = "",
    @SerializedName("StudentContactInfo")
    var userAddress: UserAddress,
    @SerializedName("StudentMotelInfo")
    var motelInfo : Motel? = null
)
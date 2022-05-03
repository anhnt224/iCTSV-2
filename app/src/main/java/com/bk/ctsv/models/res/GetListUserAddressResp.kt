package com.bk.ctsv.models.res

import com.bk.ctsv.models.entity.UserAddress
import com.google.gson.annotations.SerializedName

class GetListUserAddressResp (
    @SerializedName("RespCode")
    override var respCode: Int = -1,
    @SerializedName("RespText")
    override var respText: String = "",
    @SerializedName("StudentContactLst")
    var userAddresses: List<UserAddress>
): CTSVCap()
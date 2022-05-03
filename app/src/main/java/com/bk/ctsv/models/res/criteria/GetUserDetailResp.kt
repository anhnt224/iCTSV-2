package com.bk.ctsv.models.res.criteria

import com.bk.ctsv.models.entity.UserDetail
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetUserDetailResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("ClassDetailInfo")
    var userDetail: UserDetail

): CTSVCap()
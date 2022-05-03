package com.bk.ctsv.models.res.teacher

import com.bk.ctsv.models.entity.Student
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListClassResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("ClassLst")
    var listClass: List<String>
): CTSVCap()
package com.bk.ctsv.modules_teacher.contactParent

import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetStudentInfoTokenResp(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("StudentLink") var urlToken: String?
) :
    CTSVCap()
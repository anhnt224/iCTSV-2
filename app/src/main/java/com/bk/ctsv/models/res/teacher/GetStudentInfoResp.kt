package com.bk.ctsv.models.res.teacher

import com.bk.ctsv.models.entity.StudentInfo
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetStudentInfoResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("ClassDetailInfo")
    var studentInfo: StudentInfo
): CTSVCap()
package com.bk.ctsv.models.res.criteria

import com.bk.ctsv.models.entity.Semester
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListSemesters (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("SemesterLst")
    var semesters: List<Semester>

): CTSVCap()
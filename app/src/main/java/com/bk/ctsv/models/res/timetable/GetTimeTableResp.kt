package com.bk.ctsv.models.res.timetable

import com.bk.ctsv.models.entity.Subject
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetTimeTableResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("ScheduleStudentLst")
    var subjects: ArrayList<Subject> = arrayListOf()
): CTSVCap()
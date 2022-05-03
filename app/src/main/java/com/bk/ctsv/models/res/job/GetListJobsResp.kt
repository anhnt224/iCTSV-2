package com.bk.ctsv.models.res.job

import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListJobsResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("NumberPage")
    var page : Int = 0,
    @SerializedName("RecruitmentLst")
    var jobs: List<Job>?
): CTSVCap()
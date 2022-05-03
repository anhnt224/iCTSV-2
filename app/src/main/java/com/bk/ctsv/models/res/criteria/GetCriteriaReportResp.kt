package com.bk.ctsv.models.res.criteria

import com.bk.ctsv.models.entity.CriteriaReport
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetCriteriaReportResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("CriteriaTypes")
    var criteriaReports: List<CriteriaReport>
): CTSVCap()
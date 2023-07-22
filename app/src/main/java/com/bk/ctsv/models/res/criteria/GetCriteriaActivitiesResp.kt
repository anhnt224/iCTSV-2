package com.bk.ctsv.models.res.criteria

import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.entity.CriteriaReport
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetCriteriaActivitiesResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("Activities")
    var activities: List<Activity>
): CTSVCap()
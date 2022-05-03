package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.CriteriaType
import com.bk.ctsv.models.entity.Semester
import com.google.gson.annotations.SerializedName

class MarkCriteriaUserReq (
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("UserCode")
    var userCode: String = "",
    @SerializedName("TokenCode")
    var token: String = "",
    @SerializedName("Semester")
    var semester: String,
    @SerializedName("CriteriaTypeDetailsLst")
    var criteriaTypes: List<CriteriaType>
)
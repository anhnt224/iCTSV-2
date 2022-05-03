package com.bk.ctsv.models.res.criteria

import com.bk.ctsv.models.entity.CriteriaType
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListCriteriaTypesResp(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("CriteriaTypeDetailsLst")
    var criteriaTypes: List<CriteriaType>
): CTSVCap()
package com.bk.ctsv.models.res.scholarShip

import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListScholarShipsAppliedResp (
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("NumberPage")
    val page : Int = 0,
    @SerializedName("ScholarshipLst")
    val scholarShips : List<ScholarShip>
): CTSVCap()
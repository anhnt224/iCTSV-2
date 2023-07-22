package com.bk.ctsv.modules.searchMotel.model

import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetMotelResultsResp(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("StudentMotelLst")
    val motelList: List<Motel>
) : CTSVCap()
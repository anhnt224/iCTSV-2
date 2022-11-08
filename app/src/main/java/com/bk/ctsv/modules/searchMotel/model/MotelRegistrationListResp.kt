package com.bk.ctsv.modules.searchMotel.model;

import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class MotelRegistrationListResp(
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("MotelRegisterLst")
    val motelRegistrationList: List<MotelRegistration>
) : CTSVCap()





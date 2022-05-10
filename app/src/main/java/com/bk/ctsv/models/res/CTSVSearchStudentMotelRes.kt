package com.bk.ctsv.models.res

import com.bk.ctsv.models.entity.Motel
import com.google.gson.annotations.SerializedName

class CTSVSearchStudentMotelRes(
    override val respText: String?,
    override val respCode: Int?,
    @SerializedName("StudentMotelLst")
    val studentMotelList: List<Motel>
): CTSVCap()
package com.bk.ctsv.models.res

import com.bk.ctsv.models.entity.ImageMotel
import com.google.gson.annotations.SerializedName

class CTSVMotelImageRes(
    @SerializedName("RespText")
    override val respText: String = "",
    @SerializedName("RespCode")
    override val respCode: Int = -1,
    @SerializedName("ImageLst")
    val imageList: List<ImageMotel> = listOf()
    ): CTSVCap()
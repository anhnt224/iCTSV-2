package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.run.RunData
import com.google.gson.annotations.SerializedName

class SaveRunDataReq (
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("TokenCode")
    var token: String = "",
    @SerializedName("RunResultLst")
    var runDataList: List<RunData> = listOf()
)
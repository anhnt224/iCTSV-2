package com.bk.ctsv.models.entity.run

import com.google.gson.annotations.SerializedName

class RunData (
    @SerializedName("ComID")
    var competitionID: Int = 0,
    @SerializedName("ComName")
    var competitionName: String = "",
    @SerializedName("WayID")
    var wayID: String = "",
    @SerializedName("UserCode")
    var userCode: String = "",
    @SerializedName("TimeStart")
    var timeStart: String = "",
    @SerializedName("TimeEnd")
    var timeEnd: String = "",
    @SerializedName("Distance")
    var distance: Double = 0.0
)
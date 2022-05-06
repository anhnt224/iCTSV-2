package com.bk.ctsv.models.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Motel(
    @SerializedName("MotelID")
    var id: String = "",
    @SerializedName("Type")
    var type: String = "",
    @SerializedName("ManagerPhone")
    var managerContact: String = "",
    @SerializedName("ManagerName")
    var managerName: String = "",
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Address")
    var address: String = "",
    @SerializedName("Rate")
    var rate: Int = 0,
    @SerializedName("Comment")
    var comment: String = "",
    @SerializedName("Latitude")
    var latitude: Double = 0.0,
    @SerializedName("Longitude")
    var longitude: Double = 0.0,
    @SerializedName("Distance")
    var distance: Double = 0.0,
    @SerializedName("Status")
    var status: Int = 0
): Serializable
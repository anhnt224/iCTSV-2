package com.bk.ctsv.models.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageMotel(
    @SerializedName("UrlImg")
    val urlImage: String,
    @SerializedName("TypeImg")
    val type: Int
): Serializable
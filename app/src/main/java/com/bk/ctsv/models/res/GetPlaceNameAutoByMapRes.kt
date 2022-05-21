package com.bk.ctsv.models.res

import com.bk.ctsv.models.entity.PlaceNameByMap
import com.google.gson.annotations.SerializedName

class GetPlaceNameAutoByMapRes (
    @SerializedName("data")
    var data: PlaceNameByMap = PlaceNameByMap()
)
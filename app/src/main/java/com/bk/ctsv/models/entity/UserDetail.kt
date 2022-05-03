package com.bk.ctsv.models.entity

import com.google.gson.annotations.SerializedName

class UserDetail (
    @SerializedName("CriteriaPointSemsterLst")
    var trainingPoints: List<TrainingPoint>
)
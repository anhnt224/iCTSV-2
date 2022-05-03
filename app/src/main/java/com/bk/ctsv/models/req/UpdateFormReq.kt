package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.FormQuestion
import com.google.gson.annotations.SerializedName

class UpdateFormReq(
    @SerializedName("TokenCode")
    var token: String = "",

    @SerializedName("RowID")
    var rowID: Int = 0,

    @SerializedName("SetStudentPaperLst")
    var questions: List<FormQuestion> = listOf()
)
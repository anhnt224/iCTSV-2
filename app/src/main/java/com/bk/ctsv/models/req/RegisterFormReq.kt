package com.bk.ctsv.models.req

import com.bk.ctsv.models.entity.FormQuestion
import com.bk.ctsv.models.entity.Question
import com.google.gson.annotations.SerializedName

class RegisterFormReq (
    @SerializedName("TokenCode")
    var token: String = "",

    @SerializedName("SetStudentPaperLst")
    var questions: List<FormQuestion> = listOf(),

    @SerializedName("SVContactID")
    var studentContactID: Int,

    @SerializedName("Ship")
    var deliveryType: Int
)
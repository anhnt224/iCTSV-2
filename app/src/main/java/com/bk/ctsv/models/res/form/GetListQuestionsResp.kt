package com.bk.ctsv.models.res.form

import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.models.entity.FormQuestion
import com.bk.ctsv.models.entity.Question
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListQuestionsResp (
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("HSQuestionPaperLst")
    var questions: List<FormQuestion>
): CTSVCap()
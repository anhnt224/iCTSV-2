package com.bk.ctsv.models.res.form

import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.models.res.CTSVCap
import com.google.gson.annotations.SerializedName

class GetListFormsResp (
    @SerializedName("RespCode")
    override val respCode: Int?,
    @SerializedName("RespText")
    override val respText: String?,
    @SerializedName("HSPaperLst")
    var forms: List<Form>?
):CTSVCap()
package com.bk.ctsv.ui.adapter.criteria

import com.bk.ctsv.models.entity.UserCriteriaDetail

interface OnCriteriaItemClick {
    fun onProofClick(userCriteriaDetail: UserCriteriaDetail)
    fun onTextProofClick(userCriteriaDetail: UserCriteriaDetail)
}
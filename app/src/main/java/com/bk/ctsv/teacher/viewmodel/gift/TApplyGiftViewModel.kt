package com.bk.ctsv.teacher.viewmodel.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.GiftRegister
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.GiftRepository
import javax.inject.Inject

class TApplyGiftViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {
    private val _giftRegisterInfo = MutableLiveData<GiftRegister>()

    fun applyGift(giftRegister: GiftRegister) {
        _giftRegisterInfo.value = giftRegister
    }

    val applyGiftResp: LiveData<Resource<MyCTSVCap>> = _giftRegisterInfo.switchMap {
        giftRepository.applyGift(it)
    }
}
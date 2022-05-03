package com.bk.ctsv.ui.viewmodels.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.repositories.GiftRepository
import com.bk.ctsv.webservices.ApiResponse
import javax.inject.Inject

class CreateGiftViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {
    private val _gift = MutableLiveData<Gift>()

    fun createGift(gift: Gift){
        _gift.value = gift
    }
    val createGiftResp: LiveData<Resource<Int>> = _gift.switchMap {
        giftRepository.createGift(it)
    }
}
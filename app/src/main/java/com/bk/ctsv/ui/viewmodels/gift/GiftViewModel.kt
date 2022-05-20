package com.bk.ctsv.ui.viewmodels.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.repositories.GiftRepository
import javax.inject.Inject

class GiftViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {
    val gifts = MediatorLiveData<Resource<List<Gift>>>()
    private lateinit var getAllGiftsResp: LiveData<Resource<List<Gift>>>


    init {
        getGiftsByCreateId()
    }


    fun getGiftsByCreateId(){
        getAllGiftsResp = giftRepository.getAllGifts()
        gifts.removeSource(getAllGiftsResp)
        gifts.addSource(getAllGiftsResp){
            gifts.value = it
        }
    }

}
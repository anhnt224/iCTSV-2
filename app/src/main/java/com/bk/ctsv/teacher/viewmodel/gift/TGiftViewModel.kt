package com.bk.ctsv.teacher.viewmodel.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.repositories.GiftRepository
import com.bk.ctsv.ui.fragments.gift.GiftFragment
import javax.inject.Inject

class TGiftViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {
    val gifts = MediatorLiveData<Resource<List<Gift>>>()
    private lateinit var getAllGiftsResp: LiveData<Resource<List<Gift>>>

    val giftsRegistered = MediatorLiveData<Resource<List<Gift>>>()
    private lateinit var getGiftsRegistered: LiveData<Resource<List<Gift>>>

    val giftType = MediatorLiveData<GiftFragment.GiftType>()

    init {
        getGiftsByCreateId()
        getGiftsRegistered()
        giftType.value = GiftFragment.GiftType.ALL
    }

    fun setType(giftType: GiftFragment.GiftType){
        if (this.giftType.value != giftType){
            this.giftType.value = giftType
        }
    }

    fun getGiftsByCreateId(){
        getAllGiftsResp = giftRepository.getAllGifts()
        gifts.removeSource(getAllGiftsResp)
        gifts.addSource(getAllGiftsResp){
            gifts.value = it
        }
    }

    fun getGiftsRegistered(){
        getGiftsRegistered = giftRepository.getGiftsRegistered()
        giftsRegistered.removeSource(getGiftsRegistered)
        giftsRegistered.addSource(getGiftsRegistered){
            giftsRegistered.value = it
        }
    }
}
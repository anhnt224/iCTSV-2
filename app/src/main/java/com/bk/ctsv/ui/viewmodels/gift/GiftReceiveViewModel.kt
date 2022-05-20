package com.bk.ctsv.ui.viewmodels.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.repositories.GiftRepository
import javax.inject.Inject

class GiftReceiveViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {
    val giftsRegistered = MediatorLiveData<Resource<List<Gift>>>()
    private lateinit var getGiftsRegistered: LiveData<Resource<List<Gift>>>

    init {
        getGiftsRegistered()
    }
    fun getGiftsRegistered(){
        getGiftsRegistered = giftRepository.getGiftsRegistered()
        giftsRegistered.removeSource(getGiftsRegistered)
        giftsRegistered.addSource(getGiftsRegistered){
            giftsRegistered.value = it
        }
    }
}
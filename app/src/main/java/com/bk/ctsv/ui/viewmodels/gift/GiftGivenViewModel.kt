package com.bk.ctsv.ui.viewmodels.gift

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.GiftRepository
import javax.inject.Inject

class GiftGivenViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {
    val gifts = MediatorLiveData<Resource<List<Gift>>>()
    private lateinit var getGiftsByCreateIdResp: LiveData<Resource<List<Gift>>>

    private val _parameter = MutableLiveData<DeleteGiftParameter>()

    init {
        getGiftsByCreateId()
    }

    fun deleteGift(giftId: Int){
        _parameter.value = DeleteGiftParameter(giftId)
    }

    val deleteGiftResp: LiveData<Resource<MyCTSVCap>> = _parameter.switchMap {
        giftRepository.deleteGift(it.giftId)
    }

    fun getGiftsByCreateId(){
        getGiftsByCreateIdResp = giftRepository.getGiftsByCreateID()
        gifts.removeSource(getGiftsByCreateIdResp)
        gifts.addSource(getGiftsByCreateIdResp){
            gifts.value = it
        }
    }

    inner class DeleteGiftParameter(val giftId: Int)
}
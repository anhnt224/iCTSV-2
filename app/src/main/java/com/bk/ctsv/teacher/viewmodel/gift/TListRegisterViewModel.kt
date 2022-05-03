package com.bk.ctsv.teacher.viewmodel.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.gift.GiftRegister
import com.bk.ctsv.models.entity.gift.UserApprove
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.GiftRepository
import javax.inject.Inject

class TListRegisterViewModel @Inject constructor(
    private val giftRepository: GiftRepository
): ViewModel() {

    private val _parameter = MutableLiveData<GetGiftRegisterParameter>()
    private val _approveParameter =  MutableLiveData<ApproveParameter>()

    fun approve(giftId: Int, userApproves: List<UserApprove>){
        _approveParameter.value = ApproveParameter(giftId, userApproves)
    }

    val approveResp: LiveData<Resource<MyCTSVCap>> = _approveParameter.switchMap {
        giftRepository.approveRegisterGift(
            giftId = it.giftId,
            userApproves = it.userApproves
        )
    }

    fun getGiftRegisters(giftId: Int){
        if (_parameter.value != GetGiftRegisterParameter(giftId)){
            _parameter.value = GetGiftRegisterParameter(giftId)
        }
    }

    val giftRegisters: LiveData<Resource<List<GiftRegister>>> = _parameter.switchMap {
        giftRepository.getGiftRegisters(it.giftId)
    }

    inner class GetGiftRegisterParameter(val giftId: Int)
    inner class ApproveParameter(val giftId: Int,val userApproves: List<UserApprove>)
}
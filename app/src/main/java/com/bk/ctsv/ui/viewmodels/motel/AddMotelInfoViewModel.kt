package com.bk.ctsv.ui.viewmodels.motel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.models.res.UpdateStudentContactResp
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class AddMotelInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val updateUserAddress = MediatorLiveData<Resource<UpdateStudentContactResp>>()
    private lateinit var liveDaraUpdateUserAddress: LiveData<Resource<UpdateStudentContactResp>>

    fun updateUserAddress(userAddress: UserAddress, motelInfo: Motel){
        liveDaraUpdateUserAddress = userRepository.updateUserAddress(userAddress, motelInfo)
        updateUserAddress.removeSource(liveDaraUpdateUserAddress)
        updateUserAddress.addSource(liveDaraUpdateUserAddress){
            updateUserAddress.value = it
        }
    }
}
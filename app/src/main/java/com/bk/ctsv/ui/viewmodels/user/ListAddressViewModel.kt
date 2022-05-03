package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class ListAddressViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val addresses = MediatorLiveData<Resource<List<UserAddress>>>()
    private lateinit var liveDataGetListAddresses: LiveData<Resource<List<UserAddress>>>

    fun getListAddress(){
        liveDataGetListAddresses = userRepository.getListUserAddress()
        addresses.removeSource(liveDataGetListAddresses)
        addresses.addSource(liveDataGetListAddresses){
            addresses.value = it
        }
    }

    val deleteAddress = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataDeleteAddress: LiveData<Resource<MyCTSVCap>>
    fun deleteAddress(address: UserAddress){
        liveDataDeleteAddress = userRepository.deleteUserAddress(address)
        deleteAddress.removeSource(liveDataDeleteAddress)
        deleteAddress.addSource(liveDataDeleteAddress){
            deleteAddress.value = it
        }

    }
}
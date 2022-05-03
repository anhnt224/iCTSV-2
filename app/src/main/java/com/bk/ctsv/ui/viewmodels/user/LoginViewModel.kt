package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.User
import com.bk.ctsv.models.res.user.CTSVUserLoginRes
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isSuccess = false
    val mediatorliveLoginResource = MediatorLiveData<Resource<CTSVUserLoginRes>>()
    private lateinit var liveLoginResource: LiveData<Resource<CTSVUserLoginRes>>

    fun login(userName: String,password: String) {
        isSuccess = true
        liveLoginResource = userRepository.login(userName,password)
        mediatorliveLoginResource.removeSource(liveLoginResource)
        mediatorliveLoginResource.addSource(liveLoginResource) {
            mediatorliveLoginResource.value = it
        }
    }

    fun loginWithMSAccount(headers: Map<String, String>){
        isSuccess = true
        liveLoginResource = userRepository.loginWithMSAccount(headers)
        mediatorliveLoginResource.removeSource(liveLoginResource)
        mediatorliveLoginResource.addSource(liveLoginResource) {
            mediatorliveLoginResource.value = it
        }
    }
}

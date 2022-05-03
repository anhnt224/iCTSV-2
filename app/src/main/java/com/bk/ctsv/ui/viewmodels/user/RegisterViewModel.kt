package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isSuccess = false
    val userRegisterResource = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var _userRegisterResource: LiveData<Resource<MyCTSVCap>>

    fun userRegister(userName: String,email : String,mobile: String) {
        isSuccess = true
        _userRegisterResource = userRepository.userRegister(userName,email,mobile)
        userRegisterResource.removeSource(_userRegisterResource)
        userRegisterResource.addSource(_userRegisterResource) {
            userRegisterResource.value = it
        }
    }
}

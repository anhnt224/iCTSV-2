package com.bk.ctsv.teacher.viewmodel.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class TChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isSuccess = false
    val changePasswordResource = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var _changePasswordResource: LiveData<Resource<MyCTSVCap>>

    fun changePassword(password: String,newPassword: String) {
        isSuccess = true
        _changePasswordResource = userRepository.changePassword(password,newPassword)
        changePasswordResource.removeSource(_changePasswordResource)
        changePasswordResource.addSource(_changePasswordResource) {
            changePasswordResource.value = it
        }
    }
}
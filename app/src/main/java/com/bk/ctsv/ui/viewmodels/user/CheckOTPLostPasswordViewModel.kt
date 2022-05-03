package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class CheckOTPLostPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isSuccess = false
    val checkOTPLostPasswordResource = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var _checkOTPLostPasswordResource: LiveData<Resource<MyCTSVCap>>

    fun checkOTPLostPassword(userName: String,otp: String) {
        isSuccess = true
        _checkOTPLostPasswordResource = userRepository.checkOTPLostPassword(userName,"",otp,"")
        checkOTPLostPasswordResource.removeSource(_checkOTPLostPasswordResource)
        checkOTPLostPasswordResource.addSource(_checkOTPLostPasswordResource) {
            checkOTPLostPasswordResource.value = it
        }
    }
}

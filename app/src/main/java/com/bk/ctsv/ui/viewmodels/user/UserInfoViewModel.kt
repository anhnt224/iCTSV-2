package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.User
import com.bk.ctsv.repositories.user.UserRepository
import com.bk.ctsv.utilities.AbsentLiveData
import javax.inject.Inject

class UserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _parameter: MutableLiveData<Parameter> = MutableLiveData()
    val parameter: LiveData<Parameter>
        get() = _parameter

    val user: LiveData<Resource<User>> = Transformations
        .switchMap(_parameter) { input ->
            input.ifExists { userCode ->
                userRepository.getUserInfo()
            }
        }

    fun retry() {
        val userCode = _parameter.value?.userCode
        if (userCode != null ) {
            _parameter.value = Parameter(userCode)
        }
    }

    fun setParameter(userCode: String) {
        val update = Parameter(userCode)
        if (_parameter.value == update) {
            return
        }
        _parameter.value = update
    }

    data class Parameter(val userCode: String) {
        fun <T> ifExists(f: (String) -> LiveData<T>): LiveData<T> {
            return if (userCode.isEmpty() ) {
                AbsentLiveData.create()
            } else {
                f(userCode)
            }
        }
    }

}

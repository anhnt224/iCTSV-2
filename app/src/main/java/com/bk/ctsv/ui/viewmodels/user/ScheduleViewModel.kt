package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.ScheduleStudent
import com.bk.ctsv.repositories.user.UserRepository
import com.bk.ctsv.utilities.AbsentLiveData
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _parameter: MutableLiveData<Parameter> = MutableLiveData()
    val parameter: LiveData<Parameter>
        get() = _parameter

    val scheduleStudents: LiveData<Resource<List<ScheduleStudent>>> = Transformations
        .switchMap(_parameter) { input ->
            input.ifExists {
                userRepository.getScheduleStudent()
            }
        }

    fun getWeekNumber() = userRepository.getWeekNumber()

    fun setParameter(uId: String) {
        val update = Parameter(uId)
        if (_parameter.value == update) {
            return
        }
        _parameter.value = update
    }

    fun retry() {
        val userName = _parameter.value?.userName
        if (userName != null ) {
            _parameter.value = Parameter(userName)
        }
    }

    data class Parameter(val userName: String) {
        fun <T> ifExists(f: (String) -> LiveData<T>): LiveData<T> {
            return if (userName.isEmpty()) {
                AbsentLiveData.create()
            } else {
                f(userName)
            }
        }
    }
}
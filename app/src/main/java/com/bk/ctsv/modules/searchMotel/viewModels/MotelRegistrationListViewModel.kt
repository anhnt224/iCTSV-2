package com.bk.ctsv.modules.searchMotel.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.extension.getTimeQuery
import com.bk.ctsv.modules.searchMotel.MotelRegistrationRepository
import com.bk.ctsv.modules.searchMotel.model.MotelRegistration
import java.util.*
import javax.inject.Inject

class MotelRegistrationListViewModel @Inject constructor(
    private val motelRegistrationRepository: MotelRegistrationRepository
) : ViewModel() {

    val motelRegistrationList = MediatorLiveData<Resource<List<MotelRegistration>>>()
    private lateinit var motelRegistrationListLiveData: LiveData<Resource<List<MotelRegistration>>>

    init {
        getMotelRegistrationList()
    }

    fun getMotelRegistrationList() {
        val startTime = "2022-11-07 00:00:00"
        val endTime = Calendar.getInstance().time.getTimeQuery()

        motelRegistrationListLiveData =
            motelRegistrationRepository.getMotelRegistrationList(startTime, endTime)
        motelRegistrationList.removeSource(motelRegistrationListLiveData)
        motelRegistrationList.addSource(motelRegistrationListLiveData) {
            motelRegistrationList.value = it
        }
    }
}
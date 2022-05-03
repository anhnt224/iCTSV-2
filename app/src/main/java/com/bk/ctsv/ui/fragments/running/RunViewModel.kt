package com.bk.ctsv.ui.fragments.running

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.run.RunData
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.RunRepository
import javax.inject.Inject

class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {
    val saveRunData = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var saveRunDataLiveData: LiveData<Resource<MyCTSVCap>>

    fun saveRunData(runDataList: List<RunData>){
        saveRunDataLiveData = runRepository.saveRunData(runDataList)
        saveRunData.removeSource(saveRunDataLiveData)
        saveRunData.addSource(saveRunDataLiveData){
            saveRunData.value = it
        }
    }
}
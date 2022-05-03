package com.bk.ctsv.ui.viewmodels.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.repositories.JobRepository
import javax.inject.Inject

class ListJobApplyViewModel @Inject constructor(
    val jobRepository: JobRepository
) : ViewModel() {
    private lateinit var liveDataGetListJobsApply: LiveData<Resource<List<Job>>>
    val listJobsApply = MediatorLiveData<Resource<List<Job>>>()

    fun getListJobApply(){
        liveDataGetListJobsApply = jobRepository.getListJobApply()
        listJobsApply.removeSource(liveDataGetListJobsApply)
        listJobsApply.addSource(liveDataGetListJobsApply){
            listJobsApply.value = it
        }
    }
}
package com.bk.ctsv.ui.viewmodels.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.models.entity.JobType
import com.bk.ctsv.repositories.JobRepository
import javax.inject.Inject

class ListJobsViewModel @Inject constructor(
    private val jobRepository: JobRepository
): ViewModel() {
    private val _jobType = MutableLiveData<JobType>()
    init {
        _jobType.value = JobType.HOT
    }
    var jobs: LiveData<Resource<List<Job>>> = Transformations.switchMap(_jobType){jobType ->
        when(jobType){
            JobType.HOT -> jobRepository.getListJob(1)
            JobType.NEW -> jobRepository.getListJob(2)
            JobType.INTERSHIP -> jobRepository.getListJob(3)
            JobType.PARTTIME -> jobRepository.getListJob(4)
        }
    }

    fun setJobType(type: JobType){
        _jobType.value = type
    }

    fun getJobType(): LiveData<JobType>{
        return _jobType
    }
}
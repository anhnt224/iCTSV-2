package com.bk.ctsv.ui.viewmodels.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.models.entity.JobType
import com.bk.ctsv.repositories.JobRepository
import javax.inject.Inject

class MoreJobViewModel @Inject constructor(
    private val jobRepository: JobRepository
): ViewModel() {
    val jobMediatorLiveData = MediatorLiveData<Resource<List<Job>>>()
    private lateinit var jobLivedata: LiveData<Resource<List<Job>>>

    init {
        getMoreJob()
    }
   fun getMoreJob(){
       jobLivedata = jobRepository.getListJob(4)
       jobMediatorLiveData.removeSource(jobLivedata)
       jobMediatorLiveData.addSource(jobLivedata){
           jobMediatorLiveData.value = it
       }
   }


}
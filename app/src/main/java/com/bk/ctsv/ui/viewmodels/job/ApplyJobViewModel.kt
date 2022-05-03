package com.bk.ctsv.ui.viewmodels.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.JobRepository
import javax.inject.Inject

class ApplyJobViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {
    private lateinit var liveDataApplyJob: LiveData<Resource<MyCTSVCap>>
    val applyJob = MediatorLiveData<Resource<MyCTSVCap>> ()
    fun applyJob(id: Int, phoneNumber: String, introduction: String){
        liveDataApplyJob = jobRepository.applyJob(id, phoneNumber, introduction)
        applyJob.removeSource(liveDataApplyJob)
        applyJob.addSource(liveDataApplyJob){
            applyJob.value = it
        }
    }
}
package com.bk.ctsv.ui.fragments.criteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.CriteriaType
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.criteria.CriteriaRepository
import javax.inject.Inject

class GradingAutoViewModel @Inject constructor(
    private val criteriaRepository: CriteriaRepository
) : ViewModel() {
    val markCriteriaUser = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataMarkCriteriaUser: LiveData<Resource<MyCTSVCap>>
    fun markCriteriaUser(semester: String, criteriaTypes: List<CriteriaType>){
        liveDataMarkCriteriaUser = criteriaRepository.markCriteriaUser(semester, criteriaTypes)
        markCriteriaUser.removeSource(liveDataMarkCriteriaUser)
        markCriteriaUser.addSource(liveDataMarkCriteriaUser){
            markCriteriaUser.value = it
        }
    }
}
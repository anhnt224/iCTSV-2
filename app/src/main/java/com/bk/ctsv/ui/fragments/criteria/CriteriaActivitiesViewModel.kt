package com.bk.ctsv.ui.fragments.criteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.repositories.criteria.CriteriaRepository
import javax.inject.Inject

class CriteriaActivitiesViewModel @Inject constructor(private val criteriaRepository: CriteriaRepository) :
    ViewModel() {
    val criteriaActivities = MediatorLiveData<Resource<List<Activity>>>()
    private lateinit var caLiveData: LiveData<Resource<List<Activity>>>

    fun getCriteriaActivities(semester: String) {
        caLiveData = criteriaRepository.getCriteriaActivities(semester)
        criteriaActivities.removeSource(caLiveData)
        criteriaActivities.addSource(caLiveData){
            criteriaActivities.value = it
        }
    }
}
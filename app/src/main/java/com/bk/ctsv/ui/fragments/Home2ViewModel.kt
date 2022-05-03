package com.bk.ctsv.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.entity.Semester
import com.bk.ctsv.models.entity.UserDetail
import com.bk.ctsv.models.res.activity.GetPublicActivity
import com.bk.ctsv.repositories.activity.ActivityRepository
import com.bk.ctsv.repositories.criteria.CriteriaRepository
import javax.inject.Inject

class Home2ViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val criteriaRepository: CriteriaRepository
) : ViewModel() {
    val activities = MediatorLiveData<Resource<List<Activity>>>()
    private lateinit var getPublicActivity: LiveData<Resource<List<Activity>>>

    val semesters = MediatorLiveData<Resource<List<Semester>>>()
    private lateinit var liveDataGetListSemester: LiveData<Resource<List<Semester>>>


    init {
        getPublicActivities()
    }

    fun getPublicActivities(){
        getPublicActivity = activityRepository.getPublicActivities()
        activities.removeSource(getPublicActivity)
        activities.addSource(getPublicActivity){
            activities.value = it
        }
    }

    fun getListSemester(){
        liveDataGetListSemester = criteriaRepository.getListSemesters()
        semesters.removeSource(liveDataGetListSemester)
        semesters.addSource(liveDataGetListSemester){
            semesters.value = it
        }
    }
}
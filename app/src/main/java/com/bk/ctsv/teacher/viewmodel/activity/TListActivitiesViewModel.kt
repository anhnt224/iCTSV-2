package com.bk.ctsv.teacher.viewmodel.activity

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.repositories.activity.ActivityRepository
import javax.inject.Inject

class TListActivitiesViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    val activities = MediatorLiveData<Resource<List<Activity>>>()
    private lateinit var liveDataListActivity: LiveData<Resource<List<Activity>>>

    init {
        getListActivities()
    }

    fun getListActivities(){
        liveDataListActivity = activityRepository.getActivityByUser("", "", 10000, 0)
        activities.removeSource(liveDataListActivity)
        activities.addSource(liveDataListActivity){
            activities.value = it
        }
    }
}
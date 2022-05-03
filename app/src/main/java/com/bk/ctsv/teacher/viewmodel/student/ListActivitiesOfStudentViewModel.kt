package com.bk.ctsv.teacher.viewmodel.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.repositories.TeacherRepository
import javax.inject.Inject

class ListActivitiesOfStudentViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository
) : ViewModel() {
    val activities = MediatorLiveData<Resource<List<Activity>>>()
    private lateinit var liveDataGetListActivities: LiveData<Resource<List<Activity>>>

    fun getListActivities(studentID: String){
        liveDataGetListActivities = teacherRepository.getActivityByStudent(studentID)
        activities.removeSource(liveDataGetListActivities)
        activities.addSource(liveDataGetListActivities){
            activities.value = it
        }
    }
}
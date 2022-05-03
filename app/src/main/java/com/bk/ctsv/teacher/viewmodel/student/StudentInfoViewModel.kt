package com.bk.ctsv.teacher.viewmodel.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.StudentInfo
import com.bk.ctsv.repositories.TeacherRepository
import javax.inject.Inject

class StudentInfoViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository
) : ViewModel() {
    val studentInfo = MediatorLiveData<Resource<StudentInfo>>()
    private lateinit var liveDataGetStudentInfo: LiveData<Resource<StudentInfo>>

    fun getStudentInfo(studentID: String){
        liveDataGetStudentInfo = teacherRepository.getStudentInfo(studentID)
        studentInfo.removeSource(liveDataGetStudentInfo)
        studentInfo.addSource(liveDataGetStudentInfo){
            studentInfo.value = it
        }
    }
}
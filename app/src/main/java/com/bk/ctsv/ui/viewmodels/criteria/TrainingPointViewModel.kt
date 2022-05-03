package com.bk.ctsv.ui.viewmodels.criteria

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.CriteriaReport
import com.bk.ctsv.models.entity.Semester
import com.bk.ctsv.models.entity.TrainingPoint
import com.bk.ctsv.models.entity.UserDetail
import com.bk.ctsv.repositories.criteria.CriteriaRepository
import javax.inject.Inject

class TrainingPointViewModel @Inject constructor(val criteriaRepository: CriteriaRepository) : ViewModel() {
    val userDetail = MediatorLiveData<Resource<UserDetail>>()
    val semesters = MediatorLiveData<Resource<List<Semester>>>()
    private val _semester = MutableLiveData<Semester>()
    private lateinit var liveDataGetUserDetail: LiveData<Resource<UserDetail>>
    private lateinit var liveDataGetListSemester: LiveData<Resource<List<Semester>>>

    init {
        getTrainingPoint()
        getListSemester()
    }
    fun getTrainingPoint(){
        liveDataGetUserDetail = criteriaRepository.getUserDetail()
        userDetail.removeSource(liveDataGetUserDetail)
        userDetail.addSource(liveDataGetUserDetail){
            userDetail.value = it
        }
    }
    fun getListSemester(){
        liveDataGetListSemester = criteriaRepository.getListSemesters()
        semesters.removeSource(liveDataGetListSemester)
        semesters.addSource(liveDataGetListSemester){
            semesters.value = it
        }
    }
    fun setSemester(semester: Semester){
        _semester.value = semester
    }
    fun getSemester(): LiveData<Semester>{
        return _semester
    }

    var criteriaReports: LiveData<Resource<List<CriteriaReport>>> = Transformations.switchMap(_semester){
        criteriaRepository.getCriteriaReports(it.name)
    }
}
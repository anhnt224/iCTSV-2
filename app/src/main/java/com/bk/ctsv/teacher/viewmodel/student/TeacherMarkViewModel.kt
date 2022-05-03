package com.bk.ctsv.teacher.viewmodel.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.CriteriaType
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.TeacherRepository
import javax.inject.Inject

class TeacherMarkViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository
) : ViewModel() {
    val criteriaTypes = MediatorLiveData<Resource<List<CriteriaType>>>()
    private lateinit var liveDataGetListCriteriaTypes: LiveData<Resource<List<CriteriaType>>>

    fun getListCriteriaTypes(semester: String, studentID: String) {
        liveDataGetListCriteriaTypes = teacherRepository.getListCriteriaTypes(semester, studentID)
        criteriaTypes.removeSource(liveDataGetListCriteriaTypes)
        criteriaTypes.addSource(liveDataGetListCriteriaTypes){
            criteriaTypes.value = it
        }
    }

    val markCriteriaUser = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataMarkCriteriaUser: LiveData<Resource<MyCTSVCap>>
    fun markCriteriaUser(semester: String, studentID: String, criteriaTypes: List<CriteriaType>){
        liveDataMarkCriteriaUser = teacherRepository.markCriteriaUser(semester, studentID, criteriaTypes)
        markCriteriaUser.removeSource(liveDataMarkCriteriaUser)
        markCriteriaUser.addSource(liveDataMarkCriteriaUser){
            markCriteriaUser.value = it
        }
    }
}
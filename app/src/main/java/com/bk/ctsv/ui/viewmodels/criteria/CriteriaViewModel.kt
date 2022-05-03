package com.bk.ctsv.ui.viewmodels.criteria

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.CriteriaType
import com.bk.ctsv.models.entity.Semester
import com.bk.ctsv.models.entity.UserCriteriaActivity
import com.bk.ctsv.models.entity.UserCriteriaDetail
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.criteria.CriteriaRepository
import javax.inject.Inject

class CriteriaViewModel @Inject constructor(
    private val criteriaRepository: CriteriaRepository
) : ViewModel() {
    private val _semester = MutableLiveData<Semester>()

    fun getSemester(): LiveData<Semester>{
        return _semester
    }

    fun setSemester(semester: Semester){
        _semester.value = semester
    }

    private val _userCriteriaDetail = MutableLiveData<UserCriteriaDetail>()
    fun setUserCriteriaDetail(value: UserCriteriaDetail){
        _userCriteriaDetail.value = value
    }
    fun getUserCriteriaDetail(): LiveData<UserCriteriaDetail>{
        return _userCriteriaDetail
    }

    val criteriaTypes: LiveData<Resource<List<CriteriaType>>> = Transformations.switchMap(_semester){
        criteriaRepository.getListCriteriaTypes(it.name)
    }

    val activities : LiveData<Resource<List<UserCriteriaActivity>>> = Transformations.switchMap(_userCriteriaDetail){
        criteriaRepository.getListActivitiesByCId(it.cID)
    }

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
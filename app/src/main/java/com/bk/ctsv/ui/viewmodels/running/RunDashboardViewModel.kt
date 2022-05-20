package com.bk.ctsv.ui.viewmodels.running

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.dao.RunDataDao
import com.bk.ctsv.extension.toTimeQueryStr
import com.bk.ctsv.models.entity.run.RunData
import com.bk.ctsv.models.entity.run.RunResult
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.RunRepository
import java.util.*
import javax.inject.Inject

class RunDashboardViewModel @Inject constructor(
    private val runRepository: RunRepository,
    private val runDataDao: RunDataDao
) : ViewModel() {
    private var _parameters = MutableLiveData<GetRunResultParameters>()
    val saveRunData = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var saveRunDataLiveData: LiveData<Resource<MyCTSVCap>>

    fun setTime(timeStart: Date, timeEnd: Date){
        val newParameters = GetRunResultParameters(timeStart, timeEnd)
        _parameters.value = newParameters
    }

    fun getTimeStart(): Date? {
        return _parameters.value?.timeStart
    }

    fun getTimeEnd(): Date? {
        return _parameters.value?.timeEnd
    }

    fun getRunResults(): LiveData<List<RunData>>{
        return runDataDao.getRunResults()
    }


    val runResultList: LiveData<Resource<List<RunResult>>> = Transformations.switchMap(_parameters){
        runRepository.getRunResultList(timeStart = it.timeStart.toTimeQueryStr(), timeEnd = it.timeEnd.toTimeQueryStr())
    }

    fun saveRunData(runDataList: List<RunData>){
        saveRunDataLiveData = runRepository.saveRunData(runDataList)
        saveRunData.removeSource(saveRunDataLiveData)
        saveRunData.addSource(saveRunDataLiveData){
            saveRunData.value = it
        }
    }

    inner class GetRunResultParameters(var timeStart: Date, var timeEnd: Date)
}

enum class ChartType {
    BY_WEEK, BY_MONTH
}
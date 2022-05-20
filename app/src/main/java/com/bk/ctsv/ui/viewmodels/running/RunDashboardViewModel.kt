package com.bk.ctsv.ui.viewmodels.running

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.extension.toTimeQueryStr
import com.bk.ctsv.models.entity.run.RunResult
import com.bk.ctsv.repositories.RunRepository
import java.util.*
import javax.inject.Inject

class RunDashboardViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {
    private var _parameters = MutableLiveData<GetRunResultParameters>()

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

    val runResultList: LiveData<Resource<List<RunResult>>> = Transformations.switchMap(_parameters){
        runRepository.getRunResultList(timeStart = it.timeStart.toTimeQueryStr(), timeEnd = it.timeEnd.toTimeQueryStr())
    }

    inner class GetRunResultParameters(var timeStart: Date, var timeEnd: Date)
}

enum class ChartType {
    BY_WEEK, BY_MONTH
}
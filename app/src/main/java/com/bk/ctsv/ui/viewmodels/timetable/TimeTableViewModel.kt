package com.bk.ctsv.ui.viewmodels.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.dao.TimetableDAO
import com.bk.ctsv.models.entity.Subject
import com.bk.ctsv.repositories.TimeTableRepository
import javax.inject.Inject

class TimeTableViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
    private val timetableDAO: TimetableDAO
) : ViewModel() {
    val getTimeTable = MediatorLiveData<Resource<List<Subject>>>()
    private lateinit var liveDataGetTimeTable: LiveData<Resource<List<Subject>>>

    fun getTimeTable(){
        liveDataGetTimeTable = timeTableRepository.getTimeTable()
        getTimeTable.removeSource(liveDataGetTimeTable)
        getTimeTable.addSource(liveDataGetTimeTable){
            getTimeTable.value = it
        }
    }

    var getTimetableOffline: LiveData<List<Subject>> = timetableDAO.getAll()
}
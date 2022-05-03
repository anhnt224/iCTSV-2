package com.bk.ctsv.repositories

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.NetworkBoundResource
import com.bk.ctsv.common.Resource
import com.bk.ctsv.dao.TimetableDAO
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Subject
import com.bk.ctsv.models.res.timetable.GetTimeTableResp
import com.bk.ctsv.utilities.runOnIoThread
import com.bk.ctsv.webservices.ApiResponse
import com.bk.ctsv.webservices.TimeTableWebService
import javax.inject.Inject

class TimeTableRepository @Inject constructor(
    private val timetableWebService: TimeTableWebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val timetableDAO: TimetableDAO
) {
    fun getTimeTable(shouldFetch: Boolean = true): LiveData<Resource<List<Subject>>>{
        return object: NetworkBoundResource<List<Subject>, GetTimeTableResp>(appExecutors = appExecutors) {
            override fun saveCallResult(item: GetTimeTableResp) {
                if(item.subjects.isNotEmpty()){
                    runOnIoThread {
                        timetableDAO.deleteAll()
                        timetableDAO.insertAll(subjects = item.subjects)
                    }
                }
            }

            override fun shouldFetch(data: List<Subject>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<Subject>> {
                return  timetableDAO.getAll()
            }

            override fun createCall(): LiveData<ApiResponse<GetTimeTableResp>> {
                return timetableWebService.getTimeTable(userName = sharedPrefsHelper.getUserName(), userCode = sharedPrefsHelper.getUserName(), tokenCode = sharedPrefsHelper.getToken())
            }

        }.asLiveData()
    }
}
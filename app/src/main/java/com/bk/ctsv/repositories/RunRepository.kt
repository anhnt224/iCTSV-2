package com.bk.ctsv.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.NetworkBoundResource
import com.bk.ctsv.common.Resource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.run.RunData
import com.bk.ctsv.models.entity.run.RunResult
import com.bk.ctsv.models.req.SaveRunDataReq
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.models.res.run.GetRunResultsListResp
import com.bk.ctsv.webservices.ApiResponse
import com.bk.ctsv.webservices.RunWebService
import com.bk.ctsv.webservices.WebService
import javax.inject.Inject

class RunRepository @Inject constructor(
    private val runWebService: RunWebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper
) {

    private val saveRunDataLiveData = MutableLiveData<MyCTSVCap>()
    private val runResultList = MutableLiveData<List<RunResult>>()
    private val recentlyRunResults = MutableLiveData<List<RunResult>>()

    init {
        saveRunDataLiveData.value = MyCTSVCap()
        runResultList.value = listOf()
        recentlyRunResults.value = listOf()
    }

    fun saveRunData(runDataList: List<RunData>, shouldFetch: Boolean = true): LiveData<Resource<MyCTSVCap>> {
        saveRunDataLiveData.value = MyCTSVCap()
        return object: NetworkBoundResource<MyCTSVCap, MyCTSVCap>(appExecutors){
            override fun saveCallResult(item: MyCTSVCap) {
                Thread { saveRunDataLiveData.postValue(item) }.start()
            }

            override fun shouldFetch(data: MyCTSVCap?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<MyCTSVCap> {
                return saveRunDataLiveData
            }

            override fun createCall(): LiveData<ApiResponse<MyCTSVCap>> {
                val saveRunDataReq = SaveRunDataReq(
                    userName = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    runDataList = runDataList
                )
                return runWebService.saveRunData(saveRunDataReq)
            }

        }.asLiveData()
    }

    fun getRunResultList(timeStart: String, timeEnd: String, shouldFetch: Boolean = true): LiveData<Resource<List<RunResult>>> {
        return object : NetworkBoundResource<List<RunResult>,GetRunResultsListResp>(appExecutors){
            override fun saveCallResult(item: GetRunResultsListResp) {
                Thread{
                    runResultList.postValue(item.runResultList)
                }.start()
            }

            override fun shouldFetch(data: List<RunResult>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<RunResult>> {
                return runResultList
            }

            override fun createCall(): LiveData<ApiResponse<GetRunResultsListResp>> {
                return runWebService.getRunResultList(
                    userName = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    timeStart = timeStart,
                    timeEnd = timeEnd
                )
            }

        }.asLiveData()
    }

    fun getRecentlyRunResults(timeStart: String, timeEnd: String, shouldFetch: Boolean = true): LiveData<Resource<List<RunResult>>> {
        return object : NetworkBoundResource<List<RunResult>,GetRunResultsListResp>(appExecutors){
            override fun saveCallResult(item: GetRunResultsListResp) {
                Thread{
                    recentlyRunResults.postValue(item.runResultList)
                }.start()
            }

            override fun shouldFetch(data: List<RunResult>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<RunResult>> {
                return recentlyRunResults
            }

            override fun createCall(): LiveData<ApiResponse<GetRunResultsListResp>> {
                return runWebService.getRunResultList(
                    userName = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    timeStart = timeStart,
                    timeEnd = timeEnd
                )
            }

        }.asLiveData()
    }
}
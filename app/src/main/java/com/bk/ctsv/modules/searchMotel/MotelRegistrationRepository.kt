package com.bk.ctsv.modules.searchMotel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.NetworkBoundResource
import com.bk.ctsv.common.Resource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.modules.searchMotel.model.MotelRegistration
import com.bk.ctsv.modules.searchMotel.model.MotelRegistrationListResp
import com.bk.ctsv.modules.searchMotel.model.RegisterMotelReq
import com.bk.ctsv.webservices.ApiResponse
import com.bk.ctsv.webservices.WebService
import javax.inject.Inject

class MotelRegistrationRepository @Inject constructor(
    private val webservice: WebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper
) {
    private val motelRegistrationList = MutableLiveData<List<MotelRegistration>>()
    private val registerMotelResp = MutableLiveData<MyCTSVCap>()
    private val deleteMotelRegistrationResp = MutableLiveData<MyCTSVCap>()

    init {
        motelRegistrationList.value = listOf()
        registerMotelResp.value = MyCTSVCap()
        deleteMotelRegistrationResp.value = MyCTSVCap()
    }

    fun getMotelRegistrationList(
        startTime: String,
        endTime: String,
        shouldFetch: Boolean = true
    ): LiveData<Resource<List<MotelRegistration>>> {
        return object :
            NetworkBoundResource<List<MotelRegistration>, MotelRegistrationListResp>(appExecutors) {
            override fun saveCallResult(item: MotelRegistrationListResp) {
                Thread {
                    motelRegistrationList.postValue(item.motelRegistrationList)
                }.start()
            }

            override fun shouldFetch(data: List<MotelRegistration>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<MotelRegistration>> {
                return motelRegistrationList
            }

            override fun createCall(): LiveData<ApiResponse<MotelRegistrationListResp>> {
                return webservice.getMotelRegistrationList(
                    userName = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    startTime = startTime,
                    endTime = endTime
                )
            }

        }.asLiveData()
    }

    fun registerMotel(
        motelRegistration: MotelRegistration,
        shouldFetch: Boolean = true
    ): LiveData<Resource<MyCTSVCap>> {
        return object : NetworkBoundResource<MyCTSVCap, MyCTSVCap>(appExecutors) {
            override fun saveCallResult(item: MyCTSVCap) {
                Thread {
                    registerMotelResp.postValue(item)
                }.start()
            }

            override fun shouldFetch(data: MyCTSVCap?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<MyCTSVCap> {
                return registerMotelResp
            }

            override fun createCall(): LiveData<ApiResponse<MyCTSVCap>> {
                val req = RegisterMotelReq(
                    username = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    motelRegistration = motelRegistration
                )
                return webservice.registerMotel(req)
            }

        }.asLiveData()
    }

    fun deleteMotelRegistration(
        id: Int , shouldFetch: Boolean = true
    ): LiveData<Resource<MyCTSVCap>> {
        return object: NetworkBoundResource<MyCTSVCap, MyCTSVCap>(appExecutors){
            override fun saveCallResult(item: MyCTSVCap) {
                Thread{
                    deleteMotelRegistrationResp.postValue(item)
                }.start()
            }

            override fun shouldFetch(data: MyCTSVCap?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<MyCTSVCap> {
                return deleteMotelRegistrationResp
            }

            override fun createCall(): LiveData<ApiResponse<MyCTSVCap>> {
                return webservice.deleteMotelRegistration(
                    userName = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    docID = id
                )
            }

        }.asLiveData()
    }

}
package com.bk.ctsv.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.NetworkBoundResource
import com.bk.ctsv.common.Resource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.models.res.scholarShip.GetListScholarShipsAppliedResp
import com.bk.ctsv.models.res.scholarShip.GetListScholarShipsResp
import com.bk.ctsv.webservices.ApiResponse
import com.bk.ctsv.webservices.WebService
import javax.inject.Inject

class ScholarShipRepository @Inject constructor(
    private val webservice: WebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper
){
    private var liveDataGetListScholarShip = MutableLiveData<List<ScholarShip>>()
    private var liveDataGetListScholarShipsApplied = MutableLiveData<List<ScholarShip>>()
    private var liveDataApplyScholarShip = MutableLiveData<MyCTSVCap>()

    init {
        liveDataGetListScholarShip.value = listOf()
        liveDataGetListScholarShipsApplied.value = listOf()
        liveDataApplyScholarShip.value = MyCTSVCap()
    }

    fun getListScholarShips(page: Int, row: Int, shouldFetch: Boolean = true): LiveData<Resource<List<ScholarShip>>>{
        return object: NetworkBoundResource<List<ScholarShip>, GetListScholarShipsResp>(appExecutors){
            override fun saveCallResult(item: GetListScholarShipsResp) {
                Thread(Runnable { liveDataGetListScholarShip.postValue(item.scholarShips) }).start()
            }

            override fun shouldFetch(data: List<ScholarShip>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<ScholarShip>> {
                return liveDataGetListScholarShip
            }

            override fun createCall(): LiveData<ApiResponse<GetListScholarShipsResp>> {
                return webservice.getListScholarShips(sharedPrefsHelper.getUserName(), sharedPrefsHelper.getToken(), row, page)
            }

        }.asLiveData()
    }

    fun getListScholarShipsApplied(page: Int, row: Int, shouldFetch: Boolean = true): LiveData<Resource<List<ScholarShip>>>{
        return object: NetworkBoundResource<List<ScholarShip>, GetListScholarShipsAppliedResp>(appExecutors){
            override fun saveCallResult(item: GetListScholarShipsAppliedResp) {
                Thread(Runnable { liveDataGetListScholarShipsApplied.postValue(item.scholarShips) }).start()
            }

            override fun shouldFetch(data: List<ScholarShip>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<ScholarShip>> {
                return liveDataGetListScholarShipsApplied
            }

            override fun createCall(): LiveData<ApiResponse<GetListScholarShipsAppliedResp>> {
                return webservice.getListScholarShipApplied(sharedPrefsHelper.getUserName(), sharedPrefsHelper.getUserName(), sharedPrefsHelper.getToken(), row, page)
            }

        }.asLiveData()
    }

    fun applyScholarShip(scholarShipID: Int, shouldFetch: Boolean = true): LiveData<Resource<MyCTSVCap>>{
        return object: NetworkBoundResource<MyCTSVCap, MyCTSVCap>(appExecutors){
            override fun saveCallResult(item: MyCTSVCap) {
                Thread(Runnable { liveDataApplyScholarShip.postValue(item) }).start()
            }

            override fun shouldFetch(data: MyCTSVCap?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<MyCTSVCap> {
                return liveDataApplyScholarShip
            }

            override fun createCall(): LiveData<ApiResponse<MyCTSVCap>> {
                return webservice.applyScholarShip(sharedPrefsHelper.getUserName(), sharedPrefsHelper.getToken(), scholarShipID)
            }

        }.asLiveData()
    }
}
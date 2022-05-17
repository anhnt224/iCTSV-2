package com.bk.ctsv.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.NetworkBoundResource
import com.bk.ctsv.common.Resource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.ImageMotel
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.models.res.CTSVMotelImageRes
import com.bk.ctsv.models.res.CTSVSearchStudentMotelRes
import com.bk.ctsv.webservices.ApiResponse
import com.bk.ctsv.webservices.WebService
import javax.inject.Inject


class MotelRepository @Inject constructor(
    private val webservice: WebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper
) {
    private val motelList = MutableLiveData<List<Motel>>()
    private val motelImageList = MutableLiveData<List<ImageMotel>>()

    init {
        motelList.value = listOf()
        motelImageList.value = listOf()
    }

    fun getListMotel(
        latitude: Double,
        longitude: Double,
        distance: Int,
        shouldFetch: Boolean = true
    ): LiveData<Resource<List<Motel>>>{
        return object : NetworkBoundResource<List<Motel>, CTSVSearchStudentMotelRes>(appExecutors){
            override fun saveCallResult(item: CTSVSearchStudentMotelRes) {
                Thread{motelList.postValue(item.studentMotelList) }.start()
               /* Log.v("_MotelRepository", "Motel List: ${motelList.value}")
                Log.v("_MotelRepository", "Item Motel: ${item.studentMotelList}")*/
            }

            override fun shouldFetch(data: List<Motel>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<Motel>> {
                return motelList
            }

            override fun createCall(): LiveData<ApiResponse<CTSVSearchStudentMotelRes>> {
                Log.v("_MotelRepository", sharedPrefsHelper.getToken())
                return webservice.searchStudentMotel(
                    userName = sharedPrefsHelper.getUserName(),
                    tokenCode = sharedPrefsHelper.getToken(),
                    latitude = latitude,
                    longitude = longitude,
                    distance = distance
                )
            }
        }.asLiveData()
    }

    fun getMotelListImage(
        motelID: Int,
        shouldFetch: Boolean = true
    ): LiveData<Resource<List<ImageMotel>>>{
        return object : NetworkBoundResource<List<ImageMotel>, CTSVMotelImageRes>(appExecutors){
            override fun saveCallResult(item: CTSVMotelImageRes) {
                Thread{ motelImageList.postValue(item.imageList) }.start()
                Log.v("_MotelRepository", "Item Motel: ${item.imageList}")
            }

            override fun shouldFetch(data: List<ImageMotel>?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<List<ImageMotel>> {
                return motelImageList
            }

            override fun createCall(): LiveData<ApiResponse<CTSVMotelImageRes>> {
                return webservice.getImageMotel(
                    userName = sharedPrefsHelper.getUserName(),
                    token = sharedPrefsHelper.getToken(),
                    motelID = motelID
                )
            }

        }.asLiveData()
    }
}
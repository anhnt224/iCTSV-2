package com.bk.ctsv.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.NetworkBoundResource
import com.bk.ctsv.common.Resource
import com.bk.ctsv.dao.ImageMotelDao
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.ImageMotel
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.utilities.runOnIoThread
import com.bk.ctsv.webservices.ApiResponse
import com.bk.ctsv.webservices.WebService
import javax.inject.Inject

class MotelRepository @Inject constructor(
    private val webservice: WebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val imageMotelDao: ImageMotelDao
) {

    private var ctsvCapLiveData  = MediatorLiveData<MyCTSVCap>()

    init {
        ctsvCapLiveData.value = MyCTSVCap()
    }
    fun insertImage(image: ImageMotel){
        runOnIoThread {
            imageMotelDao.insertImageMotel(image)
        }
    }

    fun getAllImageMotel(idMotel: Int): LiveData<List<ImageMotel>>{
        return imageMotelDao.getAllImageMotel(idMotel)
    }

    fun getImageMotelByID(idMotel: Int, type: Int): LiveData<ImageMotel>{
        return imageMotelDao.getImageMotel(idMotel, type)
    }

    fun deleteImageMotel(image: ImageMotel){
        runOnIoThread {
            imageMotelDao.deleteImageMotel(image)
        }
    }
    fun deleteImageMotel(userName: String, token: String, motelID: Int, type: Int, shouldFetch: Boolean = true): LiveData<Resource<MyCTSVCap>>{
        ctsvCapLiveData.value = MyCTSVCap()
        return object : NetworkBoundResource<MyCTSVCap, MyCTSVCap>(appExecutors){
            override fun saveCallResult(item: MyCTSVCap) {
                Thread { ctsvCapLiveData.postValue(item)}.start()
            }

            override fun shouldFetch(data: MyCTSVCap?): Boolean {
                return data == null || shouldFetch
            }

            override fun loadFromDb(): LiveData<MyCTSVCap> {
                return ctsvCapLiveData
            }

            override fun createCall(): LiveData<ApiResponse<MyCTSVCap>> {
                return webservice.delImageMotel(userName, token, motelID, type)
            }

        }.asLiveData()
    }

}
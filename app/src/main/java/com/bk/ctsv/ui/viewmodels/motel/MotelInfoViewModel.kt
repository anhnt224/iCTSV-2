package com.bk.ctsv.ui.viewmodels.motel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.ImageMotel
import com.bk.ctsv.repositories.MotelRepository
import javax.inject.Inject

class MotelInfoViewModel @Inject constructor(
    private val motelRepository: MotelRepository
) : ViewModel() {
    val motelImageList = MediatorLiveData<Resource<List<ImageMotel>>>()
    private lateinit var motelImageListLiveData: LiveData<Resource<List<ImageMotel>>>

    fun getListMotel(motelID: Int){
        motelImageListLiveData = motelRepository.getMotelListImage(motelID)
        motelImageList.removeSource(motelImageListLiveData)
        motelImageList.addSource(motelImageListLiveData){
            motelImageList.value = it
        }
    }
}
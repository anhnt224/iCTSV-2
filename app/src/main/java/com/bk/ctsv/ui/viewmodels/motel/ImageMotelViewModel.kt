package com.bk.ctsv.ui.viewmodels.motel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.ImageMotel
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.MotelRepository
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class ImageMotelViewModel @Inject constructor(
    private val imageMotelRepository: MotelRepository
) : ViewModel() {
    val deleteImageMotel = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataDeleteImage: LiveData<Resource<MyCTSVCap>>

    fun getAllImage(idMotel: Int) = imageMotelRepository.getAllImageMotel(idMotel)

    fun deleteImage(imageMotel: ImageMotel) = imageMotelRepository.deleteImageMotel(imageMotel)

    fun insertImage(imageMotel: ImageMotel) = imageMotelRepository.insertImage(imageMotel)

    fun deleteImage( username: String, token: String ,motelID: Int, type: Int) {
        liveDataDeleteImage = imageMotelRepository.deleteImageMotel(username, token, motelID, type)
        deleteImageMotel.removeSource(liveDataDeleteImage)
        deleteImageMotel.addSource(liveDataDeleteImage){
            deleteImageMotel.value = it
        }
    }

}
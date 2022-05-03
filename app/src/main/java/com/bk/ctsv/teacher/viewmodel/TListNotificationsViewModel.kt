package com.bk.ctsv.teacher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Message
import com.bk.ctsv.repositories.user.UserRepository
import javax.inject.Inject

class TListNotificationsViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    val messages = MediatorLiveData<Resource<List<Message>>>()
    private lateinit var liveDataListMessages: LiveData<Resource<List<Message>>>

    init {
        getListMessage()
    }

    fun getListMessage(){
        liveDataListMessages = userRepository.getUserMessage()
        messages.removeSource(liveDataListMessages)
        messages.addSource(liveDataListMessages){
            messages.value = it
        }
    }


}
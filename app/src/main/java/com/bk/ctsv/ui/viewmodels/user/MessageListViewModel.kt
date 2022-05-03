package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.*
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Message
import com.bk.ctsv.repositories.user.UserRepository
import com.bk.ctsv.utilities.AbsentLiveData
import javax.inject.Inject

class MessageListViewModel @Inject constructor(
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
package com.bk.ctsv.ui.viewmodels.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.repositories.FormRepository
import javax.inject.Inject

class FormRegisteredDetailViewModel @Inject constructor(
    private val formRepository: FormRepository
) : ViewModel() {
    val form = MediatorLiveData<Resource<Form>>()
    private lateinit var liveDataGetFormDetail: LiveData<Resource<Form>>

    fun getFormDetail(rowId: Int){
        liveDataGetFormDetail = formRepository.getFormDetail(rowId)
        form.removeSource(liveDataGetFormDetail)
        form.addSource(liveDataGetFormDetail){
            form.value = it
        }
    }
}
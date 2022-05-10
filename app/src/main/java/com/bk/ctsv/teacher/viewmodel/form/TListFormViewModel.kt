package com.bk.ctsv.teacher.viewmodel.form

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.models.entity.FormType
import com.bk.ctsv.repositories.FormRepository
import javax.inject.Inject

class TListFormViewModel @Inject constructor(
    val formRepository: FormRepository
) : ViewModel() {
    val getListForms = MediatorLiveData<Resource<List<Form>>>()
    private lateinit var liveDateGetListForm: LiveData<Resource<List<Form>>>

    private var filterType = MutableLiveData<FormType>()

    init {
        filterType.value = FormType.FORM
        getListForm()
    }

    fun getFilterType(): LiveData<FormType> {
        return filterType
    }

    fun setFilterType(formType: FormType){
        filterType.value = formType
    }

    fun getListForm(){
        liveDateGetListForm = formRepository.getListForms()
        getListForms.removeSource(liveDateGetListForm)
        getListForms.addSource(liveDateGetListForm){
            getListForms.value = it
        }
    }
}
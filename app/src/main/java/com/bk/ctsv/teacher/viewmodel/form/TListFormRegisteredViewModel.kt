package com.bk.ctsv.teacher.viewmodel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.Form
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.FormRepository
import javax.inject.Inject

class TListFormRegisteredViewModel @Inject constructor(
    private val formRepository: FormRepository
) : ViewModel() {
    val getListForms = MediatorLiveData<Resource<List<Form>>>()
    private lateinit var liveDateGetListForm: LiveData<Resource<List<Form>>>

    val deleteForm = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataDeleteForm: LiveData<Resource<MyCTSVCap>>

    val rateForm = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataRateForm: LiveData<Resource<MyCTSVCap>>

    val buttonRateTapped = MutableLiveData<Boolean>()


    init {
        getListForm()
        buttonRateTapped.value = false
    }

    fun getListForm(){
        liveDateGetListForm = formRepository.getListFormsRegistered()
        getListForms.removeSource(liveDateGetListForm)
        getListForms.addSource(liveDateGetListForm){
            getListForms.value = it
        }
    }

    fun deleteForm(rowId: Int){
        liveDataDeleteForm = formRepository.deleteForm(rowId)
        deleteForm.removeSource(liveDataDeleteForm)
        deleteForm.addSource(liveDataDeleteForm){
            deleteForm.value = it
        }
    }

    fun ratingForm(form: Form, rating: Int, comment: String){
        liveDataRateForm = formRepository.rateForm(form.rowId, rating, comment)
        rateForm.removeSource(liveDataRateForm)
        rateForm.addSource(liveDataRateForm){
            rateForm.value = it
        }
    }
}
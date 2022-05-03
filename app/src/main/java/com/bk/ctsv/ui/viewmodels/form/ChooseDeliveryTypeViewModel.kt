package com.bk.ctsv.ui.viewmodels.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.FormQuestion
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.FormRepository
import javax.inject.Inject

class ChooseDeliveryTypeViewModel @Inject constructor(
    private val formRepository: FormRepository
) : ViewModel() {
    val deliveryType = MutableLiveData<Int>()

    fun setDeliveryType(type: Int){
        deliveryType.value = type
    }

    init {
        setDeliveryType(0)
    }

    val registerForm = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataRegisterForm: LiveData<Resource<MyCTSVCap>>

    fun registerForm(deliveryType: Int, studentContactID: Int, questions: List<FormQuestion>){
        liveDataRegisterForm = formRepository.registerForm(
            questions = questions,
            deliveryType = deliveryType,
            studentContactID = studentContactID
        )
        registerForm.removeSource(liveDataRegisterForm)
        registerForm.addSource(liveDataRegisterForm){
            registerForm.value = it
        }
    }
}
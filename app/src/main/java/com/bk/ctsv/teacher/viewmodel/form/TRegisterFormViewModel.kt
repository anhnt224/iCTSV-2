package com.bk.ctsv.teacher.viewmodel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.FormQuestion
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.FormRepository
import javax.inject.Inject

class TRegisterFormViewModel @Inject constructor(
    private val formRepository: FormRepository
) : ViewModel() {
    val questions = MediatorLiveData<Resource<List<FormQuestion>>>()
    private lateinit var liveDataGetListQuestions: LiveData<Resource<List<FormQuestion>>>

    val registerForm = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataRegisterForm: LiveData<Resource<MyCTSVCap>>

    fun getListQuestions(formType: String){
        liveDataGetListQuestions = formRepository.getListQuestions(formType)
        questions.removeSource(liveDataGetListQuestions)
        questions.addSource(liveDataGetListQuestions){
            questions.value = it
        }
    }

    fun registerForm(questions: List<FormQuestion>){
        liveDataRegisterForm = formRepository.registerForm(questions)
        registerForm.removeSource(liveDataRegisterForm)
        registerForm.addSource(liveDataRegisterForm){
            registerForm.value = it
        }
    }
}
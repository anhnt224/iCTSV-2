package com.bk.ctsv.ui.viewmodels.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Note
import com.bk.ctsv.repositories.TeacherRepository
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {
    val notes = MediatorLiveData<Resource<List<Note>>>()
    private lateinit var liveDataGetStudentNotes: LiveData<Resource<List<Note>>>

    init {
        getStudentNotes()
    }

    fun getStudentNotes(){
        liveDataGetStudentNotes = teacherRepository.getStudentNotes(studentID = sharedPrefsHelper.getUserName())
        notes.removeSource(liveDataGetStudentNotes)
        notes.addSource(liveDataGetStudentNotes){
            notes.value = it
        }
    }
}
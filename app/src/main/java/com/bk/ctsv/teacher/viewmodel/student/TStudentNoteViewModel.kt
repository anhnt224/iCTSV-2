package com.bk.ctsv.teacher.viewmodel.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Note
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.TeacherRepository
import javax.inject.Inject

class TStudentNoteViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {
    val notes = MediatorLiveData<Resource<List<Note>>>()
    private lateinit var liveDataGetStudentNotes: LiveData<Resource<List<Note>>>

    val addNote = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataAddNote: LiveData<Resource<MyCTSVCap>>

    val delNote = MediatorLiveData<Resource<MyCTSVCap>>()
    private lateinit var liveDataDelNote: LiveData<Resource<MyCTSVCap>>

    fun getStudentNotes(studentID: String){
        liveDataGetStudentNotes = teacherRepository.getStudentNotes(studentID = studentID)
        notes.removeSource(liveDataGetStudentNotes)
        notes.addSource(liveDataGetStudentNotes){
            notes.value = it
        }
    }

    fun addNote(studentID: String, note: String){
        liveDataAddNote = teacherRepository.addStudentNote(studentID, note)
        addNote.removeSource(liveDataAddNote)
        addNote.addSource(liveDataAddNote){
            addNote.value = it
        }
    }

    fun delNote(id: Int){
        liveDataDelNote = teacherRepository.delStudentNote(id)
        delNote.removeSource(liveDataDelNote)
        delNote.addSource(liveDataDelNote){
            delNote.value = it
        }
    }
}
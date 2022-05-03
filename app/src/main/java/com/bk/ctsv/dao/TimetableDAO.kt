package com.bk.ctsv.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bk.ctsv.models.entity.Criteria
import com.bk.ctsv.models.entity.Subject

@Dao
interface TimetableDAO {
    @Query("select * from Timetable")
    fun getAll(): LiveData<List<Subject>>

    @Query("DELETE from Timetable")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(subjects: List<Subject>)
}
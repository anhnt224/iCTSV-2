package com.bk.ctsv.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.bk.ctsv.models.entity.ScheduleStudent

@Dao
interface ScheduleStudnetDAO {


    @Query("SELECT * FROM ScheduleStudent")
    fun getAll(): LiveData<List<ScheduleStudent>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scheduleStudent: ScheduleStudent): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(scheduleStudents: List<ScheduleStudent>)

    @Delete
    fun delete(scheduleStudent: ScheduleStudent)

    @Update
    fun update(scheduleStudent: ScheduleStudent)

    @Query("DELETE FROM ScheduleStudent")
    fun deleteAll()
}
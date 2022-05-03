package com.bk.ctsv.dao

import androidx.room.*
import com.bk.ctsv.models.entity.UserCriteriaActivity


@Dao
interface UserCriteriaActivityDAO {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userCriteriaActivity: UserCriteriaActivity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userCriteriaActivitys: List<UserCriteriaActivity>)

    @Delete
    fun delete(userCriteriaActivity: UserCriteriaActivity)

    @Update
    fun update(userCriteriaActivity: UserCriteriaActivity)
}
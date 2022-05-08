package com.bk.ctsv.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bk.ctsv.models.entity.ImageMotel

@Dao
interface ImageMotelDao {

    @Query("SELECT * FROM ImageMotel WHERE idMotel = :idMotel ")
    fun getAllImageMotel(idMotel: Int): LiveData<List<ImageMotel>>

    @Query("SELECT * FROM ImageMotel WHERE idMotel = :idMotel AND type = :type")
    fun getImageMotel(idMotel: Int, type: Int): LiveData<ImageMotel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageMotel(image: ImageMotel): Long

    @Delete
    fun deleteImageMotel(image: ImageMotel)

}
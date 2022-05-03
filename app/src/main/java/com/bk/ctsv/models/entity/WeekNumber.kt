package com.bk.ctsv.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bk.ctsv.utilities.AUTO
import com.google.gson.annotations.SerializedName

@Entity(tableName = "WeekNumber")
class WeekNumber (
    @PrimaryKey
    @ColumnInfo(name = "Id") @SerializedName("Id")
    var id: String = AUTO,
    @ColumnInfo(name = "StudentName") @SerializedName("StudentName")
    var weekNumber : String? = ""
)
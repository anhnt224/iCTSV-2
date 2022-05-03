package com.bk.ctsv.models.entity.run

import com.bk.ctsv.extension.*
import com.google.gson.annotations.SerializedName
import java.util.*

class RunResult(
    @SerializedName("ComID")
    var competitionID: Int = 0,
    @SerializedName("WayID")
    var wayID: String = "",
    @SerializedName("UserCode")
    var userCode: String = "",
    @SerializedName("TimeStart")
    var timeStart: Date,
    @SerializedName("TimeEnd")
    var timeEnd: Date,
    @SerializedName("Distance")
    var distance: Double = 0.0,
    @SerializedName("TimeCreate")
    var timeCreate: Date,
    @SerializedName("RCStatus")
    var status: Int = 0
){
    fun getTime(): String{
        val milliSeconds = timeEnd.time - timeStart.time

        val hours = (milliSeconds / 1000) / 3600
        val minute = (milliSeconds / 1000) % 3600 / 60
        val seconds = (milliSeconds / 1000) % 3600 % 60

        return String.format("%02d:%02d:%02d", hours, minute, seconds)
    }

    fun getDistanceStr(): String {
        if(distance < 100){
            return String.format("%.0f m", distance)
        }
        return String.format("%.2f km", distance/1000)
    }

    fun getDistanceUnit(): String {
        if(distance < 100){
            return "m"
        }
        return "km"
    }

    fun getDateStartStr(): String {
        return timeStart.toDateStr() + ", " + timeStart.getYearStr()
    }

    fun getTimeStartStr(): String {
        return timeStart.toTimeStr()
    }

    /**
     * DAY_OF_WEEK: the resulting number ranges from 1(Sunday) to 7(Saturday)
     */
    fun timeStartToDateOfWeek(): String{
        val cal = Calendar.getInstance()
        cal.time = timeStart
        val daysOfWeek = listOf<String>("CN","T2", "T3", "T4", "T5", "T6", "T7")
        return daysOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1]
    }

    fun getPace(): String{
        val s = (timeEnd.time - timeStart.time) / 1000
        if(distance == 0.0){
            return "--:--"
        }
        val pace = (s / (distance / 1000)).toInt()

        val hours = pace / 3600
        val minute = pace % 3600 / 60
        val seconds = pace % 3600 % 60

        return if (hours == 0){
            String.format("%02d:%02d", minute, seconds) + "/km"
        }else{
            String.format("%02d:%02d:%02d", hours, minute, seconds) + "/km"
        }
    }

}
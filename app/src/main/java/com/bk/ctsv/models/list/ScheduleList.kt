package com.bk.ctsv.models.list

import com.bk.ctsv.models.entity.ScheduleStudent

class ScheduleList(val schedules: List<ScheduleStudent>) {
    fun toMap(): Map<String,List<ScheduleStudent>>{

        return  schedules
            .groupBy { it.dayOfWeek }
            .mapValues { it.value.map { it } }
    }
}
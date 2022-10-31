package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.TIME_FORMAT
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository: JpaRepository<SeminarEntity, Long> {
}

fun changeTimeStringToMinutes(timeString: String): Int {
    val matchResult = TIME_FORMAT.toRegex().matchEntire(timeString)
            ?: throw Seminar400("Wrong time format given")
    
    val hour = matchResult.groupValues[1].toInt()
    val minute = matchResult.groupValues[2].toInt()
    
    if (!((0 <= hour && hour < 24) && (0 <= minute && minute < 60))) {
        throw Seminar400("Wrong time format given")
    }
    
    return (hour * 60) + minute
}

fun changeTotalMinutesToTimeString(totalMinutes: Int): String {
    return "${totalMinutes / 60}:${totalMinutes % 60}"
}
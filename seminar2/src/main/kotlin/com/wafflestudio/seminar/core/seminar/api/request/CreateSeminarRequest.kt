package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

data class CreateSeminarRequest(
    @field: NotEmpty
    val name: String,
    @field: Positive
    val capacity: Int,
    @field: Positive
    val count: Int,
    @field: DateTimeFormat
    val time: LocalTime,
    val online: Boolean = true,
) {
    
    fun toSeminarEntity(): SeminarEntity = SeminarEntity(
        name,
        capacity,
        count,
        time,
        online,
    )
}
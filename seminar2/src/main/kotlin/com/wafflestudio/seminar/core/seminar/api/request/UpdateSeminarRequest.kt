package com.wafflestudio.seminar.core.seminar.api.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class UpdateSeminarRequest(
    @field: NotNull
    val id: Long,
    val name: String?,
    @field: Positive
    val capacity: Int?,
    @field: Positive
    val count: Int?,
    @field: DateTimeFormat
    val time: LocalTime?,
    val online: Boolean? = true,
)
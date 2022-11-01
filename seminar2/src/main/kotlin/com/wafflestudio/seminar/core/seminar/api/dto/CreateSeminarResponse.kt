package com.wafflestudio.seminar.core.seminar.api.dto

import java.time.LocalTime

data class CreateSeminarResponse(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    // TODO: Instructors & Participants List
)
package com.wafflestudio.seminar.core.seminar.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalTime

data class Seminar(
    val id: Long,
    val hostId: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    @JsonFormat(pattern = "HH:mm")
    val time: LocalTime,
    val online: Boolean,
    val participants: List<Participant>,
    val instructors: List<Instructor>,
    val participantCount: Int,
)
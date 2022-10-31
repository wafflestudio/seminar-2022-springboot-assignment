package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant
import java.time.LocalTime

data class SeminarResponse(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    val instructors: List<Instructor>,
    val participants: List<Participant> = listOf()
)
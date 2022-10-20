package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime
import java.time.LocalTime

data class Seminar(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean = true,
    val instructors: List<SeminarInstructor> = ArrayList(),
    val participants: List<SeminarParticipant> = ArrayList()
) {
}
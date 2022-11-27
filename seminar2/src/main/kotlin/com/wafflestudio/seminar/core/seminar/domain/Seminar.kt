package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.userSeminar.domain.SeminarInstructor
import com.wafflestudio.seminar.core.userSeminar.domain.SeminarParticipant
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
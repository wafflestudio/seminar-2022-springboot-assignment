package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalTime

data class SeminarDetailInfo(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    val instructors: List<SeminarInstructorInfo>,
    val participants: List<SeminarParticipantInfo>,
)
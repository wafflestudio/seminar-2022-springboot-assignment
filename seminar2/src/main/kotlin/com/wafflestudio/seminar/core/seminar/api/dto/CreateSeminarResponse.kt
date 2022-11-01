package com.wafflestudio.seminar.core.seminar.api.dto

import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructorInfo
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipantInfo
import java.time.LocalTime

data class CreateSeminarResponse(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    val instructors: List<SeminarInstructorInfo>,
    val participants: List<SeminarParticipantInfo>,
)
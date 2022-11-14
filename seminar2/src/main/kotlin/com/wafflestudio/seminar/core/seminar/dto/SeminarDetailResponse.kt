package com.wafflestudio.seminar.core.seminar.dto

import com.wafflestudio.seminar.core.user.dto.InstructorUserResponse
import com.wafflestudio.seminar.core.user.dto.ParticipantUserResponse

data class SeminarDetailResponse(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean,
    val instructors: List<InstructorUserResponse> = listOf(),
    val participants: List<ParticipantUserResponse> = listOf(),
)
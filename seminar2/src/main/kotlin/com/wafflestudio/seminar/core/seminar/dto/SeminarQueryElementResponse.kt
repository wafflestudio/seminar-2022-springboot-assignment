package com.wafflestudio.seminar.core.seminar.dto

import com.wafflestudio.seminar.core.user.dto.InstructorUserResponse

data class SeminarQueryElementResponse(
    val id: Long,
    val name: String,
    val instructors: List<InstructorUserResponse>,
    val participantCount: Int
)
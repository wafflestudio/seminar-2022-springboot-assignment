package com.wafflestudio.seminar.core.profile.dto

import com.wafflestudio.seminar.core.seminar.dto.InstructingSeminarResponse

data class InstructorProfileResponse(
    val id: Long,
    val company: String,
    val year: Int? = null,
    val instructingSeminars: List<InstructingSeminarResponse> = listOf(),
)
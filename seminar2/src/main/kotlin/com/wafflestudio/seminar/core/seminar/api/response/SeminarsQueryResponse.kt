package com.wafflestudio.seminar.core.seminar.api.response

data class SeminarsQueryResponse(
    val id: Long,
    val name: String,
    val instructors: MutableList<InstructorInfo>,
    val participantCount: Int
)

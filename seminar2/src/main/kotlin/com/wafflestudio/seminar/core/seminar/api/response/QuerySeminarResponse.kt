package com.wafflestudio.seminar.core.seminar.api.response

data class QuerySeminarResponse (
    val id: Long,
    val name: String,
    val instructors: ArrayList<InstructorResponse>,
    val participantCount: Long,
)
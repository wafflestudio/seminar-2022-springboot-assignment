package com.wafflestudio.seminar.core.seminar.domain

data class SeminarInfo(
    val id: Long,
    val name: String,
    val instructors: List<SeminarInstructorInfo>,
    val participantCount: Int,
)
package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.userSeminar.domain.SeminarInstructor

data class SeminarResponse(
    val id: Long,
    val name: String,
    val instructors: List<SeminarInstructor>,
    val participantCount: Int
) {
}
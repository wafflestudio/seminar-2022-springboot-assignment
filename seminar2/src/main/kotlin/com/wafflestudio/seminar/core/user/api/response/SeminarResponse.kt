package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.domain.SeminarInstructor

data class SeminarResponse(
    val id: Long,
    val name: String,
    val instructors: List<SeminarInstructor>,
    val participantCount: Int
) {
}
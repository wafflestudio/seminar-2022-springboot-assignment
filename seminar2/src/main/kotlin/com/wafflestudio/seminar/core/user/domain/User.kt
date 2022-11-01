package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.Instructor
import com.wafflestudio.seminar.core.seminar.domain.Participant

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val participant: Participant?,
    val instructor: Instructor?,
)
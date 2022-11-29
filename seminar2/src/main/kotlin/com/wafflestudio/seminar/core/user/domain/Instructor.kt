package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructor

data class Instructor (
    val company: String,
    val year: Int?,
    var instructingSeminars: List<SeminarInstructor>? = null,
)
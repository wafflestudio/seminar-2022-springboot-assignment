package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.UserSeminar

data class Instructor(
    val id: Long,
    val company: String,
    val year: Int?,
    val instructingSeminars: List<UserSeminar>,
)
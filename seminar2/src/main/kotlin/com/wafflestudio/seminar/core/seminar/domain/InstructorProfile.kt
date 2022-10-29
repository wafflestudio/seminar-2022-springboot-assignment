package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.database.UserEntity

data class InstructorProfile(
    val user: UserEntity,
    val company: String,
    val year: Int?,
) 
package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.userSeminar.domain.UserInstructorSeminar

data class InstructorProfile(
    val id: Long,
    val company: String = "",
    val years: Int? = null,
    val seminars: List<UserInstructorSeminar> = ArrayList()
) {
}
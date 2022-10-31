package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.InstructingSeminar

data class InstructorProfile(
    val id: Long,
    var company: String,
    var year: Number? = null,
    val instructingSeminars: InstructingSeminar? = null
) 
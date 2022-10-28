package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.*

data class Seminar(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean,
    val instructors: List<Instructor>,
    val participants: List<Participant>?
)

package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant

data class Seminar (
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean = true,
    val instructors: MutableList<Instructor> = mutableListOf(),
    val participants: MutableList<Participant> = mutableListOf(),
    val participantCount: Int = 0,
)
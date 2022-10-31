package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalTime

data class Seminar(
    val id: Long,
    val hostId: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    val participants: MutableSet<Participant>,
    val instructors: MutableSet<Instructor>,
    val participantCount: Int,
)
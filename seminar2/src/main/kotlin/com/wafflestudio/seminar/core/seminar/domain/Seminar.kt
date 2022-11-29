package com.wafflestudio.seminar.core.seminar.domain

data class Seminar (
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean = true,
    val instructors: MutableList<SeminarInstructor> = mutableListOf(),
    val participants: MutableList<SeminarParticipant> = mutableListOf(),
    val participantCount: Int = 0,
)
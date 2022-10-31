package com.wafflestudio.seminar.core.user.domain.seminarResponse

data class SeminarResponse (
    val id : Long,
    val name : String,
    val capacity : Int,
    val count : Int,
    val time : String,
    val online : Boolean,
    val instructors : List<InstructorSeminarResponse>,
    val participants : List<ParticipantSeminarResponse>
)

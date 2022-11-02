package com.wafflestudio.seminar.core.seminar.api.response

data class SeminarResponse (
    val id : Long,
    val name : String,
    val capacity : Int,
    val count : Int,
    val time : String,
    val online : Boolean,
    val instructors : MutableList<InstructorSeminarResponse>,
    val participants : MutableList<ParticipantSeminarResponse>
)

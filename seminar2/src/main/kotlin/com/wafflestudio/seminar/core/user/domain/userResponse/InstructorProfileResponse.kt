package com.wafflestudio.seminar.core.user.domain.userResponse

data class InstructorProfileResponse(
    val id : Long,
    val company : String,
    val year : Int?,
    val instructingSeminars : List<InstructorProfileSeminarResponse>
)

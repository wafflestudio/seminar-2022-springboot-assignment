package com.wafflestudio.seminar.core.user.api.response

data class InstructorProfileResponse(
    val id : Long,
    val company : String,
    val year : Int?,
    val instructingSeminar : InstructorProfileSeminarResponse?
)

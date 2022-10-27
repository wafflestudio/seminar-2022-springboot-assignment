package com.wafflestudio.seminar.core.user.dto

data class CreateSeminarInstructorDto1(
    val id: Long,
   val company: String,
    val year: Int,
    val instructingSeminars: InstructingSeminarDto
) {
}
package com.wafflestudio.seminar.core.user.dto



data class CreateSeminarDto(
    val id:Long?,
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: String?,
    val online: Boolean? = true,
    val instructors: CreateSeminarInstructorDto?
) {
}
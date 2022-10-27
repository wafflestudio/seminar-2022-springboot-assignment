package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.dto.CreateSeminarInstructorDto

data class CreateSeminar(
    val id:Long?,
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: String?,
    val online: Boolean? = true,
    val instructors: CreateSeminarInstructorDto?
)
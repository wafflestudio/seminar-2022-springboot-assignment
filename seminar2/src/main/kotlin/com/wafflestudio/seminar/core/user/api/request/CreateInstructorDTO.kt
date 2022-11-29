package com.wafflestudio.seminar.core.user.api.request

data class CreateInstructorDTO (
    val company: String?,
    val year: Int? = null,
)
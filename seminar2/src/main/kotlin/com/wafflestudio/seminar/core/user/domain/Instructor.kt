package com.wafflestudio.seminar.core.user.domain

data class Instructor(
    val id: Long,
    val company: String,
    val year: Int?,
    val instructingSeminars: MutableSet<InstructingSeminar>,
)
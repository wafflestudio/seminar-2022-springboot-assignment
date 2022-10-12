package com.wafflestudio.seminar.core.user.domain

data class InstructorProfile(
    val id: Long,
    val company: String,
    val year: Int? = null
    
) {
}
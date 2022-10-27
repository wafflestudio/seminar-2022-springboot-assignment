package com.wafflestudio.seminar.core.user.dto

import java.time.LocalDate

data class GetProfileDto(
    val id: Long?,
    val username: String?,
    val email: String?,
    val lastLogin: LocalDate?,
    val dateJoined: LocalDate?,
    val participant: GetProfileParticipantDto?,
    val instructor: GetProfileInstructorDto?
    
) {
    
}
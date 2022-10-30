package com.wafflestudio.seminar.core.user.dto.user

import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto

data class GetProfileInstructorDto(
    val id: Long?,
    val company: String?,
    val year: Int?,
    val instructingSeminars: List<InstructingSeminarsDto>?
) {
}
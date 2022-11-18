package com.wafflestudio.seminar.core.user.dto.user

import com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto

data class GetProfileInstructorDto(
    val id: Long?,
    val company: String?,
    val year: Int?,
    // val instructingSeminars: List<InstructingSeminarsDto>?
) {

    companion object {
        fun of(instructorProfileEntity: InstructorProfileEntity) : GetProfileInstructorDto{
            instructorProfileEntity.run {
                return GetProfileInstructorDto(
                        id = id,
                        company =company,
                        year = year
                )
            }
        }
    }
}
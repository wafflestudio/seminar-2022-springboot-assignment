package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.profile.InstructorProfile

data class SeminarGroupByDTO(
    var id: Long?=0,
    var name: String?="",

    var instructors: List<SeminarInstructorDTO>?= emptyList(),
    var participantCount: Long?=0
) {

    companion object {
        fun of(
            seminarDto: SeminarDTO
        ) = seminarDto.run {
            SeminarGroupByDTO(
                id = this.id,
                name = this.name,
                instructors = instructors,
                participantCount = participants!!.filter { it.isActive }.size.toLong()
            )
        }
    }
    
    
}
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import javax.persistence.*

@Entity
@Table(name = "participant_profiles")
class ParticipantProfileEntity(
    var university: String?,
    var isRegistered: Boolean,
) : BaseTimeEntity() {
    
    fun toParticipantProfile(): ParticipantProfile = ParticipantProfile(
        id,
        university,
        isRegistered,
    )
}

@Entity
@Table(name = "instructor_profiles")
class InstructorProfileEntity(
    var company: String?,
    var year: Int?,
) : BaseTimeEntity() {

    fun toInstructorProfile(): InstructorProfile = InstructorProfile(
        id,
        company,
        year,
    )
}
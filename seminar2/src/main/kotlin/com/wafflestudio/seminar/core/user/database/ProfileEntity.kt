package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import javax.persistence.*

@Entity
@Table(name = "participant_profiles")
class ParticipantProfileEntity(
    val university: String?,
    val isRegistered: Boolean,
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
    val company: String?,
    val year: Int?,
) : BaseTimeEntity() {

    fun toInstructorProfile(): InstructorProfile = InstructorProfile(
        id,
        company,
        year,
    )
}
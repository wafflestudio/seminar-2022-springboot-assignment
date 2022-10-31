package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "participant_profiles")
class ParticipantProfileEntity(
    val university: String?,
    val isRegistered: Boolean,
) : BaseTimeEntity() {
}

@Entity
@Table(name = "instructor_profiles")
class InstructorProfileEntity(
    val company: String?,
    val year: Int?,
) : BaseTimeEntity() {
}
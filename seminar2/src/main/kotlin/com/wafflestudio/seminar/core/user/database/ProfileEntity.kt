package com.wafflestudio.seminar.core.user.database

import javax.persistence.*

@MappedSuperclass
abstract class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}

@Entity
@Table(name = "participant_profiles")
class ParticipantProfileEntity(
    val university: String?,
    val isRegistered: Boolean,
) : ProfileEntity()

@Entity
@Table(name = "instructor_profiles")
class InstructorProfileEntity(
    val company: String?,
    val year: Int?
) : ProfileEntity()
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.*
import com.wafflestudio.seminar.core.user.domain.*
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    @Column(name = "email", unique = true)
    val email: String,

    @Column(name = "username")
    var username: String,

    @Column(name = "password")
    var password: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_profile_id")
    var participantProfileEntity: ParticipantProfileEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "instructor_profile_id")
    var instructorProfileEntity: InstructorProfileEntity? = null,

    @OneToMany(mappedBy = "user")
    val userSeminars: List<UserSeminarEntity>? = mutableListOf()
) : BaseTimeEntity() {

    fun toDTO(seminars: List<SeminarForParticipantProfile>?, instructingSeminars: List<SeminarForInstructorProfile>?): User = this.run {
        User(
            id = id,
            email = email,
            username = username,
            lastLogin = modifiedAt,
            dateJoined = createdAt,

            participantProfile = participantProfileEntity?.let {
                ParticipantProfile(
                    it.id,
                    it.university,
                    it.isRegistered,
                    seminars
                    
                )
            },
            instructorProfile = instructorProfileEntity?.let {
                InstructorProfile(
                    it.id,
                    it.company,
                    it.year,
                    instructingSeminars
                )
            }
        )
    }

    fun toInstructorDTO(joinedAt: LocalDateTime): Instructor = this.run {
        Instructor(
            id = id,
            username = username,
            email = email,
            joinedAt = joinedAt
        )
    }
    
}
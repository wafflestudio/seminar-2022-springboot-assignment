package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.InstructingSeminar
import com.wafflestudio.seminar.core.seminar.domain.ParticipatingSeminar
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import com.wafflestudio.seminar.core.user.domain.ProfileResponse
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var encodedPassword: String,

    var lastLogin: LocalDateTime,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val userSeminars: MutableSet<UserSeminarEntity> = mutableSetOf(),

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_profile_id", referencedColumnName = "id")
    var participantProfile: ParticipantProfileEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_profile_id", referencedColumnName = "id")
    var instructorProfile: InstructorProfileEntity? = null
) : BaseTimeEntity() {
    fun toProfileResponse(): ProfileResponse {
        val participatingSeminars: MutableList<ParticipatingSeminar> = mutableListOf()
        var instructingSeminar: InstructingSeminar? = null
        userSeminars.forEach {
            it.seminar.run {
                if (it.role == User.Role.PARTICIPANT) participatingSeminars.add(
                    ParticipatingSeminar(
                        id = id,
                        name = name,
                        joinedAt = it.joinedAt,
                        isActive = it.isActive,
                        droppedAt = it.droppedAt
                    )
                ) else instructingSeminar = InstructingSeminar(
                    id = id,
                    name = name,
                    joinedAt = it.joinedAt
                )
            }
        }
        return ProfileResponse(
            id = id,
            username = username,
            email = email,
            lastLogin = lastLogin,
            dateJoined = createdAt!!,
            participant = if (participantProfile != null) ParticipantProfile(
                id = participantProfile!!.id,
                university = participantProfile!!.university,
                isRegistered = participantProfile!!.isRegistered,
                seminars = participatingSeminars
            ) else null,
            instructor = if (instructorProfile != null) InstructorProfile(
                id = instructorProfile!!.id,
                company = instructorProfile!!.company,
                year = instructorProfile!!.year,
                instructingSeminars = instructingSeminar
            ) else null
        )
    }

    fun getInstructingSeminar(): SeminarEntity? {
        instructorProfile ?: return null
        return userSeminars.find { it.role == User.Role.INSTRUCTOR }?.seminar
    }
}
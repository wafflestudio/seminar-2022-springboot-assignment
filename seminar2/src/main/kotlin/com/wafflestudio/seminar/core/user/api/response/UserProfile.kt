package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.profile.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime

data class UserProfile(
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    var participant: ParticipantProfile? = null,
    var instructor: InstructorProfile? = null
) {
    companion object {
        fun from(userEntity: UserEntity) : UserProfile {
            return UserProfile(
                userEntity.id,
                userEntity.username,
                userEntity.email,
                userEntity.loginedAt,
                userEntity.createdAt!!,
            )
        }
        
        fun from(userEntity: UserEntity, participant: ParticipantProfile) : UserProfile {
            val userProfile = from(userEntity)
            userProfile.participant = participant
            return userProfile
        }

        fun from(userEntity: UserEntity, instructor: InstructorProfile) : UserProfile {
            val userProfile = from(userEntity)
            userProfile.instructor = instructor
            return userProfile
        }
        
        fun from(userEntity: UserEntity, participant: ParticipantProfile, instructor: InstructorProfile) : UserProfile {
            val userProfile = from(userEntity)
            userProfile.instructor = instructor
            userProfile.participant = participant
            return userProfile
        }
    }
}

data class SeminarProfile(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime?,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?
)

data class ParticipantProfile(
    val id: Long,
    val university: String = "",
    val isRegistered: Boolean,
    var seminars: MutableList<SeminarProfile> = mutableListOf()
) {
    companion object {
        fun from(
            participantProfileEntity: ParticipantProfileEntity,
            seminars: MutableList<SeminarProfile>
        ) : ParticipantProfile {
            return ParticipantProfile(
                id = participantProfileEntity.id,
                university = participantProfileEntity.user!!.university,
                isRegistered = participantProfileEntity.user!!.isRegistered,
                seminars = seminars
            )
        }
    }
}

data class SeminarProfileForInstructor(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime?
)

data class InstructorProfile (
    val id: Long,
    val company: String = "",
    val year: Int? = null,
    var instructingSeminars: SeminarProfileForInstructor?
) {
    companion object {
        fun from(
            instructorProfileEntity: InstructorProfileEntity,
            instructionSeminars: SeminarProfileForInstructor?
        ): InstructorProfile {
            return InstructorProfile(
                id = instructorProfileEntity.id,
                company = instructorProfileEntity.user!!.company,
                year = instructorProfileEntity.user!!.year,
                instructingSeminars = instructionSeminars
            )
        }
    }
}
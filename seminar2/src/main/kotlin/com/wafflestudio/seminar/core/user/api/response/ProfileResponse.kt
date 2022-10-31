package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.profile.ParticipantProfileEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.streams.toList

data class ProfileResponse (
    val id: Long,
    val username: String,
    val email : String,
    val lastLogin : LocalDateTime?,
    val dateJoined : LocalDateTime,
    val participant : ParticipantResponse?,
    val instructor: InstructorResponse?
        ){
    companion object{
        fun toProfileResponse(userEntity: UserEntity) : ProfileResponse{
            return ProfileResponse(
                id = userEntity.id,
                username = userEntity.userName,
                email = userEntity.email,
                lastLogin = userEntity.lastLogin,
                dateJoined = userEntity.dateJoined,
                instructor = InstructorResponse.toInstructorResponse(userEntity),
                participant = ParticipantResponse.toParticipantsResponse(userEntity)
            )
        }
    }
}

data class ParticipantResponse(
    val id: Long,
    val university : String?,
    val isRegistered: Boolean?,
    val seminars: List<miniSeminar>
){
    companion object{
        fun toParticipantsResponse (userEntity: UserEntity): ParticipantResponse?{
            userEntity.participantProfileEntity ?: return null
            val participant = userEntity.participantProfileEntity!!
            return ParticipantResponse(
                id = participant.id,
                university = participant.university,
                isRegistered = participant.isRegistered,
                seminars = seminarLists(userEntity)
            )
        }
        private fun seminarLists(user : UserEntity): List<miniSeminar>{
            return user.seminarUser
                .stream()
                .filter { it.role == Role.Participants}
                .map{
                    it -> miniSeminar.toMiniSeminar(it)
            }.toList()
        }
    }
    
}

data class miniSeminar(
    val id : Long,
    val name: String,
    val joinedAt: LocalDateTime?,
    val isActive : Boolean,
    val droppedAt : LocalDateTime?
){
    companion object{
        fun toMiniSeminar (seminarUser: SeminarUser) : miniSeminar {
            return miniSeminar(
                id = seminarUser.seminar.id,
                name = seminarUser.seminar.name,
                joinedAt = seminarUser.createdAt,
                isActive = seminarUser.isActive,
                droppedAt = seminarUser.droppedAt
            )
        }
    }
}

data class InstructorResponse(
    val id: Long,
    val company : String?,
    val year: Int?,
    val instructingSeminars: List<MiniInsSeminar>
){
    companion object{
        fun toInstructorResponse (userEntity: UserEntity): InstructorResponse?{
            userEntity.instructorProfileEntity ?: return null
            val instructor = userEntity.instructorProfileEntity!!
            return InstructorResponse(
                id = instructor.id,
                company = instructor.company,
                year = instructor.careerYear,
                instructingSeminars = seminarLists(userEntity)
            )
        }
        private fun seminarLists(user : UserEntity): List<MiniInsSeminar>{
            return user.seminarUser
                .filter { it.role == Role.Instructors }
                .stream()
                .map{
                    it -> MiniInsSeminar.toMiniInsSeminar(it)
            }.toList()
        }
    }

}

data class MiniInsSeminar(
    val id : Long,
    val name: String,
    val joinedAt: LocalDateTime?,

){
    companion object{
        fun toMiniInsSeminar (seminarUser: SeminarUser) : MiniInsSeminar {
            return MiniInsSeminar(
                id = seminarUser.seminar.id,
                name = seminarUser.seminar.name,
                joinedAt = seminarUser.createdAt,
            )
        }
    }
}


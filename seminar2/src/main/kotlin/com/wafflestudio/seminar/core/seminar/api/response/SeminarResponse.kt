package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.seminar.repository.Seminar
import com.wafflestudio.seminar.core.user.Role
import java.time.LocalDateTime
import kotlin.streams.toList

data class SeminarResponse (
    val id: Long?,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean,
    val instructors: List<SimpleInstructorDto>,
    val participants: List<SimpleParticipantDto>
    
) {
    companion object {
        fun of(seminar: Seminar): SeminarResponse {
            return SeminarResponse(
                id = seminar.id,
                name = seminar.name,
                capacity = seminar.capacity,
                count = seminar.count,
                time = seminar.time,
                online = seminar.online,
                instructors = getInstructorList(seminar),
                participants = getParticipantList(seminar)
            )
        }

        private fun getParticipantList(seminar: Seminar): List<SimpleParticipantDto> {
            return seminar.userSeminars
                .stream()
                .filter { it.role == Role.Participant }
                .map(SimpleParticipantDto.Companion::of)
                .toList()
        }

        fun getInstructorList(seminar: Seminar): List<SimpleInstructorDto> {
            return seminar.userSeminars
                .stream()
                .filter { it.role == Role.Instructor }
                .map(SimpleInstructorDto.Companion::of)
                .toList()
        }
    }
}

class SimpleParticipantDto(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?
) {
    companion object {
        fun of(userSeminar: UserSeminar?): SimpleParticipantDto {
            val user = userSeminar?.userEntity!!
            return SimpleParticipantDto(
                id = user.id,
                username = user.username,
                email = user.email,
                joinedAt = userSeminar.createdAt!!,
                isActive = userSeminar.isActive,
                droppedAt = userSeminar.droppedAt
            )
        }
    }
}

class SimpleInstructorDto(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime
) {
    companion object {
        fun of(userSeminar: UserSeminar?): SimpleInstructorDto {
            val user = userSeminar!!.userEntity
            return SimpleInstructorDto(
                id = user.id,
                username = user.username,
                email = user.email,
                joinedAt = userSeminar.createdAt!!
            )
        }
    }

}

package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.user.Role
import com.wafflestudio.seminar.core.user.repository.UserEntity
import java.time.LocalDateTime
import kotlin.streams.toList

data class ParticipantResponse(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    val seminars: List<SimpleSeminarDto>
) {
    companion object {
        fun of(user: UserEntity): ParticipantResponse? {
            user.participant ?: return null
            val participant = user.participant!!
            
            return ParticipantResponse(
                id = participant.id,
                university = participant.university,
                isRegistered = participant.isRegistered,
                seminars = getSeminarList(user)
            )
        }

        private fun getSeminarList(user: UserEntity): List<SimpleSeminarDto> {
            return user.userSeminars
                .stream()
                .filter { it.role == Role.Participant }
                .map(SimpleSeminarDto.Companion::of)
                .toList()
        }
    }
}

data class SimpleSeminarDto(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?
) {
    companion object {
        fun of(userSeminar: UserSeminar): SimpleSeminarDto {
            return SimpleSeminarDto(
                id = userSeminar.seminar.id,
                name = userSeminar.seminar.name,
                joinedAt = userSeminar.createdAt!!,
                isActive = userSeminar.isActive,
                droppedAt = userSeminar.droppedAt
            )
        }
    }
}

package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.user.Role
import com.wafflestudio.seminar.core.user.repository.UserEntity
import java.time.LocalDateTime
import kotlin.streams.toList

data class InstructorResponse(
    val id: Long,
    val company: String,
    val year: Int?,
    val instructingSeminars: List<SimpleInstructSeminarDto>,
) {
    companion object {
        fun of(user: UserEntity): InstructorResponse? {
            user.instructor ?: return null
            val instructor = user.instructor!!
            
            return InstructorResponse(
                id = instructor.id,
                company = instructor.company,
                year = instructor.years,
                instructingSeminars = getInstructingSeminarList(user)
            )
        }

        private fun getInstructingSeminarList(user: UserEntity): List<SimpleInstructSeminarDto> {
            return user.userSeminars
                .stream()
                .filter { it.role == Role.Instructor }
                .map(SimpleInstructSeminarDto.Companion::of)
                .toList()
        }
    }
}

data class SimpleInstructSeminarDto(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime
) {
    companion object {
        fun of(userSeminar: UserSeminar): SimpleInstructSeminarDto {
            return SimpleInstructSeminarDto(
                id = userSeminar.seminar.id,
                name = userSeminar.seminar.name,
                joinedAt = userSeminar.createdAt!!
            )
        }
    }
}

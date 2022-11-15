package com.wafflestudio.seminar.core.seminar.api.request

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

class SeminarDto {

    data class SeminarRequest(
        @field:NotBlank(message = "You should write 'name'.")
        val name: String?,
        @field:NotNull(message = "You should write 'capacity'.")
        @field:Positive(message = "'capacity' should be positive.")
        val capacity: Int?,
        @field:NotNull(message = "You should write 'count'.")
        @field:Positive(message = "'count' should be positive.")
        val count: Int?,
        @field:NotBlank(message = "You should write 'time' as a form(HH:MM).")
        val time: String?,
        val online: Boolean? = true
    )

    data class UpdateSeminarRequest(
        val name: String?,
        @field:Positive(message = "'capacity' should be positive.")
        val capacity: Int?,
        @field:Positive(message = "'count' should be positive.")
        val count: Int?,
        val time: String?,
        val online: Boolean?
    )
    
    data class SeminarProfileResponse @QueryProjection constructor(
        val id: Long,
        val name: String,
        val capacity: Int,
        val count: Int,
        val time: String,
        val online: Boolean,
        var instructors: List<UserDto.SeminarInstructorProfileResponse>,
        var participants: List<UserDto.SeminarParticipantProfileResponse>?,
        ) {


        companion object {
            fun of(
                entity: SeminarEntity,
                instructors: List<UserDto.SeminarInstructorProfileResponse>,
                participants: List<UserDto.SeminarParticipantProfileResponse>,
            ) = entity.run {
                SeminarProfileResponse(
                    id = id,
                    name = name,
                    time = time,
                    count = count,
                    capacity = capacity,
                    online = online,
                    instructors = instructors,
                    participants = participants,
                    )
            }
        }
    }

    data class SeminarProfileSimplifiedResponse @QueryProjection constructor(
        val id: Long,
        val name: String,
        var instructors: List<UserDto.SeminarInstructorProfileResponse>?,
        val participantCount: Long
    ) {
        constructor(id: Long, name: String, participantCount: Long) : this(id, name, null, participantCount)
        companion object {
            fun of(
                entity: SeminarEntity,
                instructors: List<UserDto.SeminarInstructorProfileResponse>,
            ) = entity.run {
                SeminarProfileSimplifiedResponse(
                    id = id,
                    name = name,
                    instructors = instructors,
                    participantCount = participantCount,
                )
            }
        }
    }
    
    data class SeminarResponse @QueryProjection constructor(
        val id: Long,
        val name: String,
        val joinedAt: LocalDateTime,
        val isActive: Boolean,
        val droppedAt: LocalDateTime?
    )

    data class InstructingSeminarResponse @QueryProjection constructor(
        val id: Long,
        val name: String,
        val joinedAt: LocalDateTime
    )
    
}


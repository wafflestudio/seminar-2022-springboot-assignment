package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.response.InstructorSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.ParticipantSeminarResponse
import com.wafflestudio.seminar.core.user.api.response.InstructorProfileResponse
import com.wafflestudio.seminar.core.user.api.response.InstructorProfileSeminarResponse
import com.wafflestudio.seminar.core.user.api.response.ParticipantProfileSeminarResponse
import com.wafflestudio.seminar.core.user.database.Role
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="UserSeminar")
class UserSeminar(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @JoinColumn(name = "seminar")
    var seminar: SeminarEntity,
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @JoinColumn(name = "user")
    var user: UserEntity,
    
    var role: Role,
    var isActive: Boolean = true,
    var droppedAt: LocalDateTime? = null

): BaseTimeEntity() {

    fun dropSeminar(seminarRepository: SeminarRepository) {
        isActive = false;
        droppedAt = LocalDateTime.now()
        seminar.participantCount -= 1
        seminarRepository.save(seminar)
    }
    
    fun toInstructorSeminarResponse() : InstructorSeminarResponse {
        return InstructorSeminarResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            joinedAt = createdAt!!
        )
    }

    fun toParticipantSeminarResponse() : ParticipantSeminarResponse {
        return ParticipantSeminarResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            joinedAt = createdAt!!,
            isActive = isActive,
            droppedAt = droppedAt
        )
    }
    
    fun toInstructorProfileSeminarResponse() : InstructorProfileSeminarResponse {
        return InstructorProfileSeminarResponse(
            id = user.id,
            name = user.username,
            joinedAt = createdAt!!
        )
    }
    
    fun toParticipantProfileSeminarResponse() : ParticipantProfileSeminarResponse {
        return ParticipantProfileSeminarResponse(
            id = user.id,
            name = user.username,
            joinedAt = createdAt!!,
            isActive = isActive,
            droppedAt = droppedAt
        )
    }
    
}
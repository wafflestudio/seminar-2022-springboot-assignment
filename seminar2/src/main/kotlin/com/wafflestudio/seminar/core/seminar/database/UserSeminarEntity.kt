package com.wafflestudio.seminar.core.seminar.database

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.Instructor
import com.wafflestudio.seminar.core.seminar.domain.Participant
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.InstructingSeminar
import com.wafflestudio.seminar.core.user.domain.ParticipantSeminar
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "UserSeminar")
@JsonIdentityInfo(generator = IntSequenceGenerator::class, property = "id")
data class UserSeminarEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: User.Role,
    @Column(name = "joined_at")
    val joinedAt: LocalDateTime,
    @Column(name = "dropped_at")
    var droppedAt: LocalDateTime? = null,
    @Column(name = "is_active")
    var isActive: Boolean = true,
): BaseTimeEntity() {
    
    fun toInstructor(): Instructor {
        return Instructor(
            userId = user.id,
            username = user.username,
            email = user.email,
            joinedAt = joinedAt
        )
    }

    fun toParticipant(): Participant {
        return Participant(
            userId = user.id,
            username = user.username,
            email = user.email,
            joinedAt = joinedAt,
            isActive = isActive,
            droppedAt = droppedAt,
        )
    }
    
    fun toParticipantSeminar(): ParticipantSeminar {
        return ParticipantSeminar(
            seminarId = seminar.id,
            name = seminar.name,
            joinedAt = joinedAt,
            isActive = isActive,
            droppedAt = droppedAt,
        )
    }

    fun toInstructingSeminar(): InstructingSeminar {
        return InstructingSeminar(
            seminarId = seminar.id,
            name = seminar.name,
            joinedAt = joinedAt,
        )
    }
    
    override fun hashCode() = id.hashCode()
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
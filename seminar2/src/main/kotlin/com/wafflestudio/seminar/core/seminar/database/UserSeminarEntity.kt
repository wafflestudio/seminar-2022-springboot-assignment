package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity

import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserSeminarEntity (
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,

    val role: String,
    val joinedAt: LocalDateTime,
    var droppedAt: LocalDateTime? = null,
    var isActive: Boolean = true,
): BaseTimeEntity() {
    fun toInstructor(): Instructor {
        return Instructor(
            id = user.id,
            username = user.username,
            email = user.email,
            joinedAt = joinedAt
        )
    }

    fun toParticipant(): Participant {
        return Participant(
            id = user.id,
            username = user.username,
            email = user.email,
            joinedAt = joinedAt,
            isActive = isActive,
            droppedAt = droppedAt,
        )
    }
}
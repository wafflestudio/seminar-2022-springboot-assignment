package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity

import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructor
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserSeminarEntity (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,

    val role: String,
): BaseTimeEntity() {
    val joinedAt: LocalDateTime = LocalDateTime.now()
    var droppedAt: LocalDateTime? = null
    var isActive: Boolean = true
    
    fun toInstructor(): SeminarInstructor {
        return SeminarInstructor(
            id = user.id,
            username = user.username,
            email = user.email,
            joinedAt = joinedAt,
        )
    }

    fun toParticipant(): SeminarParticipant {
        return SeminarParticipant(
            id = user.id,
            username = user.username,
            email = user.email,
            joinedAt = joinedAt,
            isActive = isActive,
            droppedAt = droppedAt,
        )
    }
}
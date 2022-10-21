package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.SeminarInstructor
import com.wafflestudio.seminar.core.user.domain.UserInstructorSeminar
import com.wafflestudio.seminar.core.user.domain.UserParticipantSeminar
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserSeminarEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    var seminar: SeminarEntity,

    @Enumerated(EnumType.STRING)
    val role: Role,

    var isActive: Boolean = true,
    var droppedAt: LocalDateTime? = null
) : BaseTimeEntity() {

    //==Mapping DTO==//
    fun toUserParticipantSeminar(): UserParticipantSeminar {
        return UserParticipantSeminar(
            seminarId = seminar.id,
            seminarName = seminar.name,
            joinedAt = createdAt,
            isActive = isActive,
            droppedAt = null
        )
    }

    fun toUserInstructorSeminar(): UserInstructorSeminar {
        return UserInstructorSeminar(
            seminarId = seminar.id,
            seminarName = seminar.name,
            joinedAt = createdAt
        )
    }
}
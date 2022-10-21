package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.SeminarInstructor
import com.wafflestudio.seminar.core.user.domain.UserInstructorSeminar
import com.wafflestudio.seminar.core.user.domain.UserParticipantSeminar
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

    val isActive: Boolean = true
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
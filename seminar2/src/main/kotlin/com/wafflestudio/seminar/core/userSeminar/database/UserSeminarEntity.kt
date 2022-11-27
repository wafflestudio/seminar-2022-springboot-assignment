package com.wafflestudio.seminar.core.userSeminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.userSeminar.domain.UserInstructorSeminar
import com.wafflestudio.seminar.core.userSeminar.domain.UserParticipantSeminar
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_seminar")
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
            droppedAt = droppedAt
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
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.UserSeminar
import javax.persistence.*

@Entity
class UserSeminarEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,

    @Enumerated(EnumType.STRING)
    val role: Role,

    val isActive: Boolean = true
) : BaseTimeEntity() {

    fun toDTO(): UserSeminar {
        return UserSeminar(
            seminarId = seminar.id,
            seminarName = seminar.name,
            joinedAt = createdAt,
            isActive = isActive,
            droppedAt = null
        )
    }
}
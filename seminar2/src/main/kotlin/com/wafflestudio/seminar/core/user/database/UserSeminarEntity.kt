package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.Role
import javax.persistence.*

@Entity
class UserSeminarEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    private val seminar: SeminarEntity,

    @Enumerated(EnumType.STRING)
    private val role: Role
) : BaseTimeEntity() {
}
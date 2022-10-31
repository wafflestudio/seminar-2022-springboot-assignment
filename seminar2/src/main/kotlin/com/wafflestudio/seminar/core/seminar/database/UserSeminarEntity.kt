package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "userSeminar")
class UserSeminarEntity(
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,

    @Enumerated(EnumType.STRING)
    val role: User.Role,
    val joinedAt: LocalDateTime,
    var droppedAt: LocalDateTime? = null,
    var isActive: Boolean
) : BaseTimeEntity() 
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class UserSeminarEntity(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: User.Role,
    @Column(name = "joined_at")
    val joinedAt: LocalDateTime?,
    @Column(name = "dropped_at")
    val droppedAt: LocalDateTime?,
    @Column(name = "is_active")
    val isActive: Boolean,
): BaseTimeEntity() {
}
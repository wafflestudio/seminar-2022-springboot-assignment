package com.wafflestudio.seminar.core.userseminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "userSeminars")
class UserSeminarEntity(
    var role: String,
    var isActive: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val userEntity: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminarId")
    val seminarEntity: SeminarEntity,
    @CreationTimestamp
    override var createdAt: LocalDateTime? = LocalDateTime.now(),
    @CreationTimestamp
    override var modifiedAt: LocalDateTime? = createdAt,
) : BaseTimeEntity() {

    var droppedAt: LocalDateTime? = null
    var joinedAt: LocalDateTime? = createdAt
}
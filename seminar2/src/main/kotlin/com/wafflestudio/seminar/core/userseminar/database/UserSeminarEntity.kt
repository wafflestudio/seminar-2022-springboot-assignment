package com.wafflestudio.seminar.core.userseminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class UserSeminarEntity(
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
        @JoinColumn()
        val seminar: SeminarEntity,

        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
        @JoinColumn()
        val user: UserEntity,

        val role: String,
        var isActive: Boolean,
        var droppedAt: LocalDateTime? = null
) : BaseTimeEntity() {
}
package com.wafflestudio.seminar.core.join

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_seminar")
@AttributeOverride(name = "createdAt", column = Column(name = "joined_at"))
class UserSeminarEntity(
        
        @ManyToOne
        @JoinColumn
        val user: UserEntity,

        @ManyToOne
        @JoinColumn
        val seminar: SeminarEntity,

        @Column(nullable = false)
        var isActive: Boolean = true,

        @Column(nullable = true)
        var droppedAt: LocalDateTime? = null,

        val role: String,
) : BaseTimeEntity() {
}
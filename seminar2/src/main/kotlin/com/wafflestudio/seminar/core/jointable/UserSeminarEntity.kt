package com.wafflestudio.seminar.core.jointable

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
//        @Column(name = "user_id")
//        var userId: Long,
//
//        @Column(name = "seminar_id")
//        var seminarId: Long,
        
        @ManyToOne
        @MapsId("user_id")
        @JoinColumn
        @Cascade(CascadeType.ALL)
        val user: UserEntity,

        @ManyToOne
        @MapsId("seminar_id")
        @JoinColumn
        @Cascade(CascadeType.ALL)
        val seminar: SeminarEntity,

        @Column(nullable = false)
        val isActive: Boolean = false,

        @Column(nullable = true)
        val droppedAt: LocalDateTime? = null,

        @Column
        val isParticipant: Boolean,
) : BaseTimeEntity() {
}
package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name="user_seminar")
class UserSeminarEntity(
    var droppedAt: LocalDateTime? = null,
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="seminar_user_id")
    val user: UserEntity? = null,
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity? = null,
    var isActive: Boolean = true,
    var isParticipant: Boolean

) : BaseTimeEntity() {
}

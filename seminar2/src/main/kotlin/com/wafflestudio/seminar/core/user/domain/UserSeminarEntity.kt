package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="UserSeminar")
class UserSeminarEntity(

    @ManyToOne
    @JoinColumn(name="user_id")
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name="seminar_id")
    val seminar: SeminarEntity?,

    @Column
    val role: String,

    @Column
    val joinedAt: LocalDateTime?,

    @Column
    val isActive: Boolean? = true,

    @Column
    val droppedAt: LocalDateTime? = null
    
):BaseTimeEntity() {

    
}
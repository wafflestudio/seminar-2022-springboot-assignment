package com.wafflestudio.seminar.core.user.domain

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
    val joinedAt: String,

    @Column
    val isActive: Boolean = true,

    @Column
    val droppedAt: LocalDateTime? = null
    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
}
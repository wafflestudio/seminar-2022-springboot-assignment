package com.wafflestudio.seminar.core.user.database

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name="ParticipantSeminar")
class ParticipantSeminarEntity(

    @ManyToOne
    @JoinColumn(name="user_id")
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name="seminar_id")
    val seminar: SeminarEntity,

    @Column
    val joinedAt: LocalDateTime,

    @Column
    val isActive: Boolean = true,

    @Column
    val droppedAt: LocalDateTime? = null
    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
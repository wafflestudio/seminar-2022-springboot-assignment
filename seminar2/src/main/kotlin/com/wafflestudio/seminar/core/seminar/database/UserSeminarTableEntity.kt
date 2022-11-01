package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "participant_seminar_table")
class ParticipantSeminarTableEntity(
    @ManyToOne
    @JoinColumn(name = "participant_id")
    val participant: UserEntity,
    
    @ManyToOne
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,
    
    val isActive: Boolean = true,
    val droppedAt: LocalDateTime?,
) : BaseTimeEntity() {
    
}

@Entity
@Table(name = "instructor_seminar_table")
class InstructorSeminarTableEntity(
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    val instructor: UserEntity,

    @ManyToOne
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity,
) : BaseTimeEntity() {
    
}
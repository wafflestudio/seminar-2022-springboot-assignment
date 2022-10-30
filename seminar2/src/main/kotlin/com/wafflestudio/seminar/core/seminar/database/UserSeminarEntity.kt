package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
class UserSeminarEntity(
    val userId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val seminar: SeminarEntity,
    @Enumerated(EnumType.STRING)
    val role: Role,
): BaseTimeEntity() {
    
    var droppedAt: LocalDateTime? = null
    var isActive: Boolean = true
    
    val joinedAt: LocalDateTime
        get() = createdAt!!
    
    val isInstructor: Boolean
        get() = role == Role.INSTRUCTOR
    
    val isParticipant: Boolean
        get() = role == Role.PARTICIPANT
    
    fun drop() {
        require(this.isParticipant)
        this.droppedAt = LocalDateTime.now()
        this.isActive = false
    }
    
    enum class Role {
        PARTICIPANT, INSTRUCTOR
    }
    
    companion object {
        fun instructor(userId: Long, seminar: SeminarEntity) =
            UserSeminarEntity(userId, seminar, Role.INSTRUCTOR)
        
        fun participant(userId: Long, seminar: SeminarEntity) =
            UserSeminarEntity(userId, seminar, Role.PARTICIPANT)
    }
}
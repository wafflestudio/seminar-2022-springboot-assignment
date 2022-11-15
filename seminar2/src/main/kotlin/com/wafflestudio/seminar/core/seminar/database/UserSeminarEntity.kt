package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.common.Seminar403
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "user_seminars",
    uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "seminar_id"])]
)
class UserSeminarEntity(
    val userId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val seminar: SeminarEntity,
    @Enumerated(EnumType.STRING)
    val role: Role,
) : BaseTimeEntity() {

    var droppedAt: LocalDateTime? = null
    var isActive: Boolean = true

    val joinedAt: LocalDateTime
        get() = createdAt!!

    val isInstructor: Boolean
        get() = role == Role.INSTRUCTOR

    val isParticipant: Boolean
        get() = role == Role.PARTICIPANT

    fun drop() {
        require(this.isParticipant) {
            throw Seminar403("강사는 세미나를 드랍할 수 없습니다.")
        }

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
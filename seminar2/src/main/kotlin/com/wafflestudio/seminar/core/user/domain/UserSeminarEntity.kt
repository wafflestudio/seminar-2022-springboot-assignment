package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "UserSeminars")
class UserSeminarEntity(

        @ManyToOne(fetch = FetchType.LAZY) // 1
        @JoinColumn(name = "user_id") // 2
        val user: UserEntity?,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "seminar_id")
        val seminar: SeminarEntity,

        @Column
        val role: String,

        @Column
        val joinedAt: LocalDateTime?,

        @Column
        var isActive: Boolean? = true,

        @Column
        var droppedAt: LocalDateTime? = null

) : BaseTimeEntity() {

    val isInstructor: Boolean
        get() = role == "INSTRUCTOR"

    val isParticipant: Boolean
        get() = role == "PARTICIPANT"

    companion object {
        fun instructor(userEntity: UserEntity, seminarEntity: SeminarEntity) =
                UserSeminarEntity(
                        user = userEntity,
                        seminar = seminarEntity,
                        role = "INSTRUCTOR",
                        joinedAt = LocalDateTime.now(),
                        isActive = null,
                        droppedAt = null
                )


        fun participant(userEntity: UserEntity, seminarEntity: SeminarEntity) =
                UserSeminarEntity(
                        user = userEntity,
                        seminar = seminarEntity,
                        role = "PARTICIPANT",
                        joinedAt = LocalDateTime.now(),
                        isActive = null,
                        droppedAt = null
                )


    }
}
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.SeminarInstructor
import com.wafflestudio.seminar.core.user.domain.SeminarParticipant
import java.time.LocalTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class SeminarEntity(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: LocalTime,
    var online: Boolean = true
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE])
    val userSeminars: MutableList<UserSeminarEntity> = ArrayList()

    //==연관관계 메서드==//
    fun addUserSeminar(userSeminarEntity: UserSeminarEntity) {
        userSeminars.add(userSeminarEntity)
        userSeminarEntity.seminar = this
    }

    //==Mapping DTO==//
    fun toDTO(): Seminar {
        val instructors = ArrayList<SeminarInstructor>()
        val participants = ArrayList<SeminarParticipant>()
        for (userSeminar in userSeminars) {
            if (userSeminar.role == Role.INSTRUCTOR) {
                instructors.add(
                    SeminarInstructor(
                        userSeminar.user.id, userSeminar.user.username, userSeminar.user.email, userSeminar.createdAt
                    )
                )
            } else {
                participants.add(
                    SeminarParticipant(
                        userSeminar.user.id, userSeminar.user.username,
                        userSeminar.user.email, userSeminar.createdAt, userSeminar.isActive, null
                    )
                )
            }
        }
        return Seminar(
            id = id,
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            instructors = instructors,
            participants = participants
        )
    }

    fun toSeminarResponse(): SeminarResponse {
        val instructors = ArrayList<SeminarInstructor>()
        var participantCount = 0
        for (userSeminar in userSeminars) {
            if (userSeminar.role == Role.INSTRUCTOR) {
                instructors.add(
                    SeminarInstructor(
                        userSeminar.user.id, userSeminar.user.username, userSeminar.user.email, userSeminar.createdAt
                    )
                )
            } else {
                participantCount++
            }
        }
        return SeminarResponse(
            id = id,
            name = name,
            instructors = instructors,
            participantCount = participantCount
        )
    }
}
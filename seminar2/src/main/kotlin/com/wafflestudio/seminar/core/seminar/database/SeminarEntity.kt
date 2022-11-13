package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.SearchSeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.SeminarResponse
import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "seminar")
class SeminarEntity(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: LocalTime,
    var online: Boolean,
    val creatorId: Long,
) : BaseTimeEntity(), Comparable<SeminarEntity> {
    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val userSeminars: MutableSet<UserSeminarEntity> = mutableSetOf()

    fun toSeminarResponse(): SeminarResponse {
        val instructors: MutableList<Instructor> = mutableListOf()
        val participants: MutableList<Participant> = mutableListOf()
        userSeminars.forEach {
            it.user.run {
                if (it.role == User.Role.INSTRUCTOR) instructors.add(
                    Instructor(
                        id = id,
                        username = username,
                        email = email,
                        joinedAt = it.joinedAt
                    )
                ) else participants.add(
                    Participant(
                        id = id,
                        username = username,
                        email = email,
                        joinedAt = it.joinedAt,
                        isActive = it.isActive,
                        droppedAt = it.droppedAt
                    )
                )
            }
        }
        return SeminarResponse(
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

    fun toSearchSeminarResponse(): SearchSeminarResponse {
        val instructors: MutableList<Instructor> = mutableListOf()
        var participantCount = 0
        userSeminars.forEach {
            it.user.run {
                if (it.role == User.Role.INSTRUCTOR) instructors.add(
                    Instructor(
                        id = id,
                        username = username,
                        email = email,
                        joinedAt = it.joinedAt
                    )
                ) else if (it.isActive) participantCount++
            }
        }
        return SearchSeminarResponse(
            id = id,
            name = name,
            instructors = instructors,
            participantCount = participantCount
        )
    }

    fun getParticipantCount(): Int {
        var participantCount = 0
        userSeminars.forEach {
            if ((it.role == User.Role.PARTICIPANT) && it.isActive) participantCount++
        }
        return participantCount
    }

    override fun compareTo(other: SeminarEntity): Int {
        return this.createdAt!!.compareTo(other.createdAt)
    }
}
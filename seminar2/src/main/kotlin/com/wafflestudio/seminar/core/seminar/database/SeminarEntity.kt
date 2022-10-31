package com.wafflestudio.seminar.core.seminar.database

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalTime
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "seminar")
@JsonIdentityInfo(generator = IntSequenceGenerator::class, property = "id")
data class SeminarEntity(
    @Column(name = "name")
    var name: String,
    @Column(name = "capacity")
    var capacity: Int,
    @Column(name = "count")
    var count: Int,
    @Column(name = "time")
    var time: LocalTime,
    @Column(name = "online")
    var online: Boolean = true,
    @Column(name = "hostId", unique = true)
    val hostId: Long,
    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val users: MutableList<UserSeminarEntity> = mutableListOf()
) : BaseTimeEntity() {

    fun toSeminar(): Seminar {
        return Seminar(
            id = id,
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            hostId = hostId,
            participants = users.filter { it.role == User.Role.PARTICIPANT }
                .map { it.toParticipant() },
            instructors = users.filter { it.role == User.Role.INSTRUCTOR }
                .map { it.toInstructor() },
            participantCount = users
                .filter { it.role == User.Role.PARTICIPANT && it.isActive }
                .size,
        )
    }

    @Transactional
    fun update(seminarRequest: EditSeminarRequest) {
        seminarRequest.name?.let {
            name = seminarRequest.name
        }
        seminarRequest.capacity?.let {
            capacity = seminarRequest.capacity
        }
        seminarRequest.count?.let {
            count = seminarRequest.count
        }
        seminarRequest.time?.let {
            time = seminarRequest.time
        }
        seminarRequest.online?.let {
            online = seminarRequest.online
        }
    }

    override fun hashCode() = id.hashCode()
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
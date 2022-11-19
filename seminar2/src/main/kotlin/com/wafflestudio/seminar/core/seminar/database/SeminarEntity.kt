package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.domain.SeminarForList
import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant
import javax.persistence.*

@Entity
@Table(name = "seminar")
class SeminarEntity(
    @Column(name = "seminar_name")
    var seminarName: String,

    @Column(name = "capacity")
    var capacity: Int,

    @Column(name = "count")
    var count: Int,

    @Column(name = "time")
    var time: String,

    @Column(name = "online")
    var online: Boolean,

    @OneToMany(mappedBy = "seminar")
    val userSeminars: List<UserSeminarEntity>? = mutableListOf()
): BaseTimeEntity() {

    fun toDTO(instructors: List<Instructor>, participants: List<Participant>?): Seminar {
        return this.run {
            Seminar(
                id = id,
                name = seminarName,
                capacity = capacity,
                count = count,
                time = time,
                online = online,
                instructors = instructors,
                participants = participants
            )
        }
    }
    
    fun toListDTO(instructors: List<Instructor>, participantCount: Long?): SeminarForList = this.run {
        SeminarForList(
            id = id,
            name = seminarName,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            instructors = instructors,
            participantCount = participantCount
        )
    }
}
package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.Participant
import javax.persistence.*

@Entity
class SeminarEntity (
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean = true,
    
    @OneToMany(mappedBy="seminar", cascade = [CascadeType.ALL])
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
): BaseTimeEntity() {
    fun toSeminar(
        instructors: MutableList<Instructor> = mutableListOf(),
        participants: MutableList<Participant> = mutableListOf()
    ): Seminar {
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
}
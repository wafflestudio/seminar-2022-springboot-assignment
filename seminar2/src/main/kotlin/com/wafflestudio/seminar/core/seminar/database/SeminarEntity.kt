package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.*

@Entity
class SeminarEntity (
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean = true,
    
    @OneToMany(mappedBy="seminar")
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
): BaseTimeEntity() {
    fun toSeminar(): Seminar {
        val participants = userSeminars
            .filter { it.role == "participant" && it.isActive }
            .map { it.toParticipant() }
            .toMutableList()
        val instructors = userSeminars
            .filter { it.role == "instructor" && it.isActive }
            .map { it.toInstructor() }
            .toMutableList()
        
        return Seminar(
            id = id,
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            instructors = instructors,
            participants = participants,
            participantCount = participants.size,
        )
    }
}
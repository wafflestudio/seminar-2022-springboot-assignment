package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import java.time.LocalDateTime

data class SeminarInfo(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean,
    var instructors: MutableList<InstructorInfo> = mutableListOf(),
    var participants: MutableList<ParticipantInfo> = mutableListOf()
) {
    companion object {
        fun from(seminarEntity: SeminarEntity): SeminarInfo {
            return SeminarInfo(
                id = seminarEntity.id,
                name = seminarEntity.name,
                capacity = seminarEntity.capacity,
                count = seminarEntity.count,
                time = seminarEntity.time,
                online = seminarEntity.online
            )
        }

        fun from(seminarEntity: SeminarEntity, instructorInfo: InstructorInfo) : SeminarInfo {
            val seminarInfo = from(seminarEntity)
            seminarInfo.instructors.add(instructorInfo)
            return seminarInfo
        }

        fun from(
            seminarEntity: SeminarEntity,
            userSeminarsEntity: List<UserSeminarEntity>
        ): SeminarInfo {
            val seminarInfo: SeminarInfo = from(seminarEntity)
            userSeminarsEntity.forEach {
                if (it.isParticipant) {
                    seminarInfo.participants.add(
                        ParticipantInfo(
                            it.user!!.id,
                            it.user!!.username,
                            it.user!!.email,
                            it.createdAt,
                            it.isActive,
                            it.droppedAt
                        )
                    )
                } else {
                    seminarInfo.instructors.add(
                        InstructorInfo(
                            it.user!!.id,
                            it.user!!.username,
                            it.user!!.email,
                            it.createdAt
                        )
                    )
                }
            }
            return seminarInfo
        }
    }
}


data class InstructorInfo(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime? = null
)

data class ParticipantInfo(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime? = null,
    val isActive: Boolean,
    val droppedAt: LocalDateTime? = null,
)


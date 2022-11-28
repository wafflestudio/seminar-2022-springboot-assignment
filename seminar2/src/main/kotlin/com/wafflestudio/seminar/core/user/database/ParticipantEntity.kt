package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipant
import com.wafflestudio.seminar.core.user.domain.Participant
import javax.persistence.*

@Entity
class ParticipantEntity(
    val university: String,
    val isRegistered: Boolean,
): BaseTimeEntity() {
    fun toParticipant(seminars: List<SeminarParticipant>): Participant {
        return Participant(
            university = university,
            isRegistered = isRegistered,
            seminars = seminars,
        )
    }
}
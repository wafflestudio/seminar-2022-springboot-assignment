package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminar
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.api.response.ParticipantProfileResponse
import com.wafflestudio.seminar.core.user.api.response.ParticipantProfileSeminarResponse
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="participant_profile")
class ParticipantProfile (
    var university : String,
    val isRegistered : Boolean
    
) : BaseTimeEntity() {
        fun toParticipantProfileResponse(userSeminars: List<UserSeminar>) : ParticipantProfileResponse {
            val retSeminars : MutableList<ParticipantProfileSeminarResponse> = mutableListOf()
            for(seminar in userSeminars) retSeminars.add(seminar.toParticipantProfileSeminarResponse())
            return ParticipantProfileResponse(
                id = id,
                university = university,
                isRegistered = isRegistered,
                seminars = retSeminars
            )
        }
}
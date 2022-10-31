package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminar
import com.wafflestudio.seminar.core.user.domain.userResponse.ParticipantProfileResponse
import com.wafflestudio.seminar.core.user.domain.userResponse.ParticipantProfileSeminarResponse
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="participants")
class ParticipantProfile (
        val university : String,
        val isRegistered : Boolean,
        val seminars : List<UserSeminar>
    
) : BaseTimeEntity() {
        fun toParticipantProfileResponse() : ParticipantProfileResponse {
                val retSeminars : List<ParticipantProfileSeminarResponse> = listOf()
                
                return ParticipantProfileResponse(
                        id = id,
                        university = university,
                        isRegistered = isRegistered,
                        seminars = retSeminars
                )
        }
}
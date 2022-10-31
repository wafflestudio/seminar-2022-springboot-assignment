package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import javax.persistence.*

@Entity
@Table(name = "participantProfile")
class ParticipantProfileEntity(
    @OneToOne(mappedBy = "participantProfile", cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: UserEntity? = null,
    var university: String,
    var isRegistered: Boolean,
) : BaseTimeEntity() {
    fun toParticipantProfile(): ParticipantProfile {
        return ParticipantProfile(
            id = id,
            university = university,
            isRegistered = isRegistered
        )
    }
}
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.domain.Participant
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "ParticipantProfile")
data class ParticipantProfileEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    @Column(name = "university")
    var university: String = "",
    @Column(name = "is_registered")
    val isRegistered: Boolean = true,
) : BaseTimeEntity() {

    fun toParticipant(): Participant {
        return Participant(
            id = id,
            university = university,
            isRegistered = isRegistered,
            seminars = emptyList(),
        )
    }
    
    @Transactional
    fun updateProfile(userRequest: UserRequest) {
        userRequest.university?.let {
            university = userRequest.university
        }
    }
}
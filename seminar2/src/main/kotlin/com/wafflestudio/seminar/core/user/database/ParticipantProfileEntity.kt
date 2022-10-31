package com.wafflestudio.seminar.core.user.database

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.domain.Participant
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "ParticipantProfile")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "id")
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
            seminars = user.seminars.filter { it.role == User.Role.PARTICIPANT }
                .map { it.toParticipantSeminar() }
        )
    }

    @Transactional
    fun updateProfile(userRequest: UserRequest) {
        userRequest.university?.let {
            university = userRequest.university
        }
    }

    override fun hashCode() = id.hashCode()
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
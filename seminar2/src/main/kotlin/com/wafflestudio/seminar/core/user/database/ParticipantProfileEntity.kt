package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.user.domain.UserSeminar
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class ParticipantProfileEntity(
    val university: String = "",
    val isRegistered: Boolean = true
) : BaseTimeEntity() {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null

    //==연관관계 메서드==//
    fun addUser(user: UserEntity) {
        this.user = user
        user.participantProfile = this
    }

    //==Mapping DTO==//
    fun toDTO(): ParticipantProfile {
        val seminars = ArrayList<UserSeminar>()
        for (seminar in user?.userSeminars!!) {
            if (seminar.role == Role.PARTICIPANT && seminar.user == user)
                seminars.add(seminar.toDTO())
        }
        return ParticipantProfile(
            id = id,
            university = university,
            isRegistered = isRegistered,
            seminars = seminars
        )
    }
}
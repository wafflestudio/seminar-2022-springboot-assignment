package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
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
}
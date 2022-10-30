package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "participant_profile")
class ParticipantEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    var university: String,
    val isRegistered: Boolean,
): BaseTimeEntity() {
    fun update(university: String) {
        this.university = university
    }
}
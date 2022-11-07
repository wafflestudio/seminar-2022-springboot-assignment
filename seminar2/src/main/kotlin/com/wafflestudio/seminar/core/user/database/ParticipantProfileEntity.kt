package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class ParticipantProfileEntity(
        @Column(nullable=false)
        var university: String,
        @Column(nullable=false)
        var isRegistered: Boolean,
) : BaseTimeEntity() {
}
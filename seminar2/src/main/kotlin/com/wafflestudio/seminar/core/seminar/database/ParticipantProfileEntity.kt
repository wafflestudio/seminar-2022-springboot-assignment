package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.*

@Entity
@Table(name = "participantProfile")
class ParticipantProfileEntity(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: UserEntity,
    val university: String,
    val isRegistered: Boolean,
) : BaseTimeEntity() 
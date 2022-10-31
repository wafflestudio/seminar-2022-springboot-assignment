package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "participantProfile")
data class ParticipantProfileEntity(
    @OneToOne(mappedBy = "participantProfile", cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: UserEntity? = null,
    var university: String,
    val isRegistered: Boolean,
) : BaseTimeEntity() 
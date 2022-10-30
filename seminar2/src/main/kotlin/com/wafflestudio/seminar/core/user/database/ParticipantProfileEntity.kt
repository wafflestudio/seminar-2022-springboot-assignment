package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.OneToOne

data class ParticipantProfileEntity(
    @OneToOne
    val user: UserEntity,
    @Column(name = "university")
    val university: String? = "",
    @Column(name = "is_registered")
    val isRegistered: Boolean = true,
): BaseTimeEntity()
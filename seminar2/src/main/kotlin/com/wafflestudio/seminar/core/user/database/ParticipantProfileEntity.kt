package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "ParticipantProfile")
data class ParticipantProfileEntity(
    @OneToOne
    val user: UserEntity,
    @Column(name = "university")
    val university: String? = "",
    @Column(name = "is_registered")
    val isRegistered: Boolean = true,
): BaseTimeEntity()
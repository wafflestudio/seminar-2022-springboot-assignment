package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
class ParticipantEntity(
    val university: String,
    val isRegistered: Boolean,
): BaseTimeEntity()
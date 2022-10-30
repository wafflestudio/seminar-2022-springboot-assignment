package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "participant_profile")
class ParticipantEntity(
    val userId: Long,
): BaseTimeEntity()
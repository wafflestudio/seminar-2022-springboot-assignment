package com.wafflestudio.seminar.core.mappingTable

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity

@Entity
class ParticipantProfile(
    var university: String,
    val isRegistered: Boolean,
): BaseTimeEntity() 
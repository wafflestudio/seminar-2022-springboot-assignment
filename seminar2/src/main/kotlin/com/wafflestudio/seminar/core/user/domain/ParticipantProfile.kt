package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.ParticipatingSeminar

data class ParticipantProfile(
    val id: Long,
    var university: String,
    val isRegistered: Boolean,
    val seminars: List<ParticipatingSeminar>,
)
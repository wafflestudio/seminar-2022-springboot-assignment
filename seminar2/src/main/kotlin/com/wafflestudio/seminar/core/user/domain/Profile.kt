package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.InstructingSeminarInfo
import com.wafflestudio.seminar.core.seminar.domain.ParticipatingSeminarInfo

data class ParticipantProfile(
    val id: Long,
    val university: String?,
    val isRegistered: Boolean,
    val seminars: List<ParticipatingSeminarInfo>,
)

data class InstructorProfile(
    val id: Long,
    val company: String?,
    val year: Int?,
    val seminars: List<InstructingSeminarInfo>,
)
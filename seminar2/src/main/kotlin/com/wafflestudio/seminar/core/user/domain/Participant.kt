package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.SeminarParticipant

data class Participant (
    val university: String,
    val isRegistered: Boolean,
    var seminars: List<SeminarParticipant>? = null,
)
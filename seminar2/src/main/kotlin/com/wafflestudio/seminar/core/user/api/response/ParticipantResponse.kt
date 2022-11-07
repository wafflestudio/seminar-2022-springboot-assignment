package com.wafflestudio.seminar.core.user.api.response

import java.time.LocalDateTime

data class ParticipantResponse (
        val id: Long,
        val university: String,
        val isRegistered: Boolean,
        val seminars: MutableList<SeminarResponse>,
)
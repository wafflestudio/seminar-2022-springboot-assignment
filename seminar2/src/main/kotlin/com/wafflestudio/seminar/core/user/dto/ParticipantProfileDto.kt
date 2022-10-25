package com.wafflestudio.seminar.core.user.dto

import com.wafflestudio.seminar.core.user.database.SeminarEntity

data class ParticipantProfileDto(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    val seminars: SeminarEntity?
) {
}
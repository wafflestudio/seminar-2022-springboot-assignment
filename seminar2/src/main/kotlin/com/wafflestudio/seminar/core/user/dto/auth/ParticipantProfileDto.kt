package com.wafflestudio.seminar.core.user.dto.auth

import com.wafflestudio.seminar.core.user.domain.SeminarEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne


data class ParticipantProfileDto(
    val university: String? = "",
    val isRegistered: Boolean = true,
) {
}


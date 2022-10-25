package com.wafflestudio.seminar.core.user.dto

import com.wafflestudio.seminar.core.user.database.SeminarEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne


data class ParticipantProfileDto(
    val id: Long,
    val university: String,
    val isRegistered: Boolean,
    
) {
}
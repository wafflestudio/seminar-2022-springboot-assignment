package com.wafflestudio.seminar.core.UserSeminar.domain

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

class ParticipantSeminarDTO @QueryProjection constructor(
    var id: Long,       // Seminar id
    var name: String,    // Seminar name
    var joinedAt: LocalDateTime,           // UserSeminar joinedAt
    var isActive: Boolean,                 // UserSeminar isActive
    var droppedAt: LocalDateTime? = null   // UserSeminar droppedAt
)
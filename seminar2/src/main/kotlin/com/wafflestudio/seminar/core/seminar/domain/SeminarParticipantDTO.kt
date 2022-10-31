package com.wafflestudio.seminar.core.seminar.domain

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

class SeminarParticipantDTO @QueryProjection constructor (
    var id: Long,                       // User id
    var username: String,               // User username
    var email: String,                  // User email
    var joinedAt: LocalDateTime,        // UserSeminar joinedAt
    var isActive: Boolean,              // UserSeminar isActive
    var droppedAt: LocalDateTime?= null // UserSeminar droppedAt
)
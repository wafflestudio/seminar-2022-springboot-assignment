package com.wafflestudio.seminar.core.user.domain

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.user.domain.profile.InstructorDTO
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantDTO
import java.time.LocalDate
import java.time.LocalDateTime

class UserDTO @QueryProjection constructor (
    var id: Long,                    // User id
    var username: String,            // User username
    var email: String,               // User email
    var lastLogin: LocalDateTime,    // User modifiedAt
    var dateJoined: LocalDate,   // User createdAt
    var participant: ParticipantDTO?,
    var instructor: InstructorDTO?
) 
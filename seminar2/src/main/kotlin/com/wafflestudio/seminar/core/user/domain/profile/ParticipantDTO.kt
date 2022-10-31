package com.wafflestudio.seminar.core.user.domain.profile

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.UserSeminar.domain.ParticipantSeminarDTO

class ParticipantDTO @QueryProjection constructor(
    var id: Long?,
    var university: String?,
    var isRegistered: Boolean?,
) {
    var seminars: List<ParticipantSeminarDTO>? = emptyList()
}
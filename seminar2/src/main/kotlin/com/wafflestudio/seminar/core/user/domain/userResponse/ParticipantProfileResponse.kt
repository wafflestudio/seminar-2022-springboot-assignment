package com.wafflestudio.seminar.core.user.domain.userResponse

data class ParticipantProfileResponse (
    val id : Long,
    val university : String,
    val isRegistered : Boolean,
    val seminars : List<ParticipantProfileSeminarResponse>
        )
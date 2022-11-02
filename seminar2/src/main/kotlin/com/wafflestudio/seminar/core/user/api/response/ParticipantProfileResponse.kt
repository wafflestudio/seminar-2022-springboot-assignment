package com.wafflestudio.seminar.core.user.api.response

data class ParticipantProfileResponse (
    val id : Long,
    val university : String,
    val isRegistered : Boolean,
    val seminars : MutableList<ParticipantProfileSeminarResponse>
        )
package com.wafflestudio.seminar.core.seminar.api.request

data class RegisterParticipantRequest (
    val university : String?,
    val isRegistered : Boolean?
        )

data class RegisterInstructor (
    val company : String?,
    val careerYear : Int?
        )
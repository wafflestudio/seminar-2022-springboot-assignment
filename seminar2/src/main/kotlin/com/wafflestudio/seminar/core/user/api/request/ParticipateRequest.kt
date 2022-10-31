package com.wafflestudio.seminar.core.user.api.request

data class ParticipateRequest (
    val id : Long,
    val university : String = "",
    val isRegistered : Boolean = true
        ){
}
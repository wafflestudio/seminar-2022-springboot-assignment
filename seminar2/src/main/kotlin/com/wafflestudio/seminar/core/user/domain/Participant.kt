package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity

data class Participant(
    val id : Long, // seminar id
    val university : String = "",
    val isRegistered : Boolean = true,
    val seminars : List<UserSeminarEntity> = ArrayList()
) {
}
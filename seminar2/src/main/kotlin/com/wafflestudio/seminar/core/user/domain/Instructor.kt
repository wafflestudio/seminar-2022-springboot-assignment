package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity

data class Instructor(
    val id : Long,
    val company : String = "",
    val year : Int? = null,
    val instructionSeminars : UserSeminarEntity? = null,
) {
}
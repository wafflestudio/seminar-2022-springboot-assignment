package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalTime

data class CreateSeminarResponse (
    val id : Long,
    val name: String,
    val capacity: Int,
    val count : Int,
    val time: LocalTime,
    val online : Boolean,
    val instructors: List<UserEntity>?,
    val participants: List<UserEntity>?
        )
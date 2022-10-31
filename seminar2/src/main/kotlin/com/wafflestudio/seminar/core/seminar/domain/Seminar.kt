package com.wafflestudio.seminar.core.seminar.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import java.time.LocalTime

data class Seminar(
    val id: Long,
    val hostId: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    @JsonIgnore
    val users: MutableSet<UserSeminarEntity>,
)
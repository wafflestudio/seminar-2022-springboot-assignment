package com.wafflestudio.seminar.core.user.api.request

import java.time.LocalTime

data class CreateSeminarRequest(
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean = true
) {
}
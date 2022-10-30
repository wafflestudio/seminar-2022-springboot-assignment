package com.wafflestudio.seminar.core.seminar.api.request

import java.time.LocalTime

data class UpdateSeminarRequest(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
)
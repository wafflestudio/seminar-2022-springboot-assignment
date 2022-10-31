package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalTime

data class Seminar(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
) 
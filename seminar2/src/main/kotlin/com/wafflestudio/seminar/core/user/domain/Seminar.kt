package com.wafflestudio.seminar.core.user.domain


data class Seminar (
    val name: String?,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean = true
)
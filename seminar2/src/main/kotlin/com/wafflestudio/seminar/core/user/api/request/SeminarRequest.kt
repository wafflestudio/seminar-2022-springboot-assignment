package com.wafflestudio.seminar.core.user.api.request

data class SeminarRequest (
    val id: Long,
    val name: String?,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean = true
)
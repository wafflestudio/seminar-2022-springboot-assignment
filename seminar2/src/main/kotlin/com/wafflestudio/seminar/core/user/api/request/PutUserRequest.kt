package com.wafflestudio.seminar.core.user.api.request

data class PutUserRequest(
    val university: String?,
    val company: String?,
    val year: Long?,
    val username: String?,
)
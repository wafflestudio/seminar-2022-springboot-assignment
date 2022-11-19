package com.wafflestudio.seminar.core.user.api.request

data class ModifyRequest(
    val username: String?,
    val email: String?,
    val password: String?,
    val university: String?,
    val isRegistered: Boolean?,
    val company: String?,
    val year: Int?
)

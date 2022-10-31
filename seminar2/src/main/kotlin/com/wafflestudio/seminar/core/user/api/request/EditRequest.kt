package com.wafflestudio.seminar.core.user.api.request

data class EditRequest (
    val username: String? = null,
    val password: String? = null,
    val university: String = "",
    val company: String = "",
    val year: Int? = null
        )
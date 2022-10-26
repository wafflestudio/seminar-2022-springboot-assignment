package com.wafflestudio.seminar.core.user.api.request

data class UpdateUserRequest(
    val username: String? = null,
    var university: String = "",
    val company: String = "",
    val year: Int? = null
) {
}
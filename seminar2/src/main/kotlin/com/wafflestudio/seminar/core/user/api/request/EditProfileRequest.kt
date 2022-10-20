package com.wafflestudio.seminar.core.user.api.request

data class EditProfileRequest(
    val email: String,
    val username: String,
    val password: String,
    val university: String = "",
    val company: String = "",
    val year: Int? = null
) {
}
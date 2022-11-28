package com.wafflestudio.seminar.core.user.api.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.wafflestudio.seminar.core.user.api.User400

data class SignUpRequest(
    val email: String?,
    val username: String?,
    val password: String?,
    val role: Role?,
    val university: String? = "",
    val isRegistered: Boolean? = true,
    val company: String? = "",
    val year: Int? = null
) {
    enum class Role(val code: String) {
        INSTRUCTOR("instructor"), PARTICIPANT("participant");

        companion object {
            @JvmStatic
            @JsonCreator
            fun Role(role: String) = values().find { it.code == role.lowercase() } ?: throw User400("올바른 role을 입력하세요.")
        }
    }
}
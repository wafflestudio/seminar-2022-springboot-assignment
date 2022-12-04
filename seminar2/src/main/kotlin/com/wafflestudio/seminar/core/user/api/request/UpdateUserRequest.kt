package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.common.Seminar400

data class UpdateUserRequest (
    val email: String? = null,
    val password: String? = null,
    val university: String = "",
    val company: String = "",
    val year: Int? = null,
) {
    init {
        if (year != null && year < 0) {
            throw Seminar400("연차 값이 잘못되었습니다.")
        }
    }
}
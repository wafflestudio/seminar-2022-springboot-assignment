package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.UserEntity

data class UpdateUserRequest(
    val username: String? = null,
    val password: String? = null,
    val university: String = "",
    val company: String = "",
    val year: Long? = null,
) {
    init {
        if (year != null && year < 0) {
            throw Seminar400("연차 값이 잘못되었습니다.")
        }
    }
}
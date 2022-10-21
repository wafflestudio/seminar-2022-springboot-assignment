package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.domain.Role

class JoinSeminarRequest(
    val seminarId: Long,
    val role: Role,
) {
}
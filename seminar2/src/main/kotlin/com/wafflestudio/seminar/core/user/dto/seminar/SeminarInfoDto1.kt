package com.wafflestudio.seminar.core.user.dto.seminar

import java.time.LocalDateTime

data class SeminarInfoDto1(
        val id: Long?,
        val name: String?,
        val capacity: Int?,
        val count: Int?,
        val time: String?,
        val online: Boolean?
) {
}
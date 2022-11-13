package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.domain.SeminarEntity

data class UpdateSeminarInfo(
    val id:Long?,
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: String?,
    val online: Boolean? = true,
) {
    companion object {
        fun of(
                seminarEntity: SeminarEntity
        ) = seminarEntity.run {
            UpdateSeminarInfo(id, name, capacity, count, time, online)
        }
    }
}
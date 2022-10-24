package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity

data class SeminarResponse(
    val name: String,
    val capacity: Int,
    val count: Int,
    var time: String,
    var online: Boolean = true
) {
    companion object {
        fun from(seminarEntity: SeminarEntity) : SeminarResponse {
            return SeminarResponse(
                seminarEntity.name,
                seminarEntity.capacity,
                seminarEntity.count,
                seminarEntity.time,
                seminarEntity.online
            )
        }
    }
}

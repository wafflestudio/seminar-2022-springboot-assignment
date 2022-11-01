package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity

data class CreateSeminarDTO (
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean? = true,
) {
    init {
        if (count <= 0) throw Seminar400("Seminar count 는 0 이상이어야 합니다.")
        if (capacity <= 0) throw Seminar400("Seminar capacity 는 0 이상이어야 합니다.")
        if (name.isEmpty()) throw Seminar400("Seminar name 은 빈 문자열이 될 수 없습니다.")
    }
    
    fun toEntity() = SeminarEntity(
        name = name,
        capacity = capacity,
        count = count,
        time = time,
        online = online,
    )
}
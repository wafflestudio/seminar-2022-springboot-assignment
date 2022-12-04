package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.common.Seminar400

data class UpdateSeminarDTO (
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: String?,
    val online: Boolean?
) {
    init {
        if (count != null && count <= 0) throw Seminar400("Seminar count 는 0 이상이어야 합니다.")
        if (capacity != null && capacity <= 0) throw Seminar400("Seminar capacity 는 0 이상이어야 합니다.")
        if (name != null && name.isEmpty()) throw Seminar400("Seminar name 은 빈 문자열이 될 수 없습니다.")
    }
}
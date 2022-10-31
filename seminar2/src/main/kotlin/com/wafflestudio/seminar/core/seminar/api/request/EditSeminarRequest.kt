package com.wafflestudio.seminar.core.seminar.api.request

import java.time.LocalTime
import javax.validation.constraints.Min

data class EditSeminarRequest(
    @field:Min(1, message = "수정할 세미나의 id를 입력하세요")
    val seminarId: Long,
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: LocalTime?,
    val online: Boolean?,
)
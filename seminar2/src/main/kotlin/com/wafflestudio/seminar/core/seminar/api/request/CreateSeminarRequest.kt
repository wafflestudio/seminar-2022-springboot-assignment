package com.wafflestudio.seminar.core.seminar.api.request

data class CreateSeminarRequest(
    val name: String? = "",
    val capacity: Int? = -1,
    val count: Int? = -1,
    val time: String?,
    val online: Boolean? = true
)

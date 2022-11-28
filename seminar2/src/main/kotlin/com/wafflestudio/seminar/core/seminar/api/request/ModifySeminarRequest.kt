package com.wafflestudio.seminar.core.seminar.api.request

data class ModifySeminarRequest(
    val id: Long?,
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: String?,
    val online: Boolean?
)

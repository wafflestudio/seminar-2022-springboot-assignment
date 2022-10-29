package com.wafflestudio.seminar.core.user.api.response

data class UpdateSeminarInfo(
    val id:Long?,
    val name: String?,
    val capacity: Int?,
    val count: Int?,
    val time: String?,
    val online: Boolean? = true,
)
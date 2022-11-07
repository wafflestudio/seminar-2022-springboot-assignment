package com.wafflestudio.seminar.core.seminar.api.request

data class MakeSeminarRequest(
        val name: String?,
        val capacity: Long?,
        val count: Long?,
        val time: String?,
        val online: Boolean = true,
)
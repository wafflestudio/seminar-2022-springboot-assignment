package com.wafflestudio.seminar.core.seminar.dto

import javax.validation.constraints.NotNull

data class SeminarPutRequest(
    @field: NotNull(message = "Id is not given.")
    val id: Long? = null,

    val name: String? = null,

    val capacity: Int? = null,

    val count: Int? = null,

    val time: String? = null,

    val online: Boolean? = null,
)
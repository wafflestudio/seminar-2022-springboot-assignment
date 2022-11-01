package com.wafflestudio.seminar.core.seminar.domain

data class Seminar (
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean? = true,
)
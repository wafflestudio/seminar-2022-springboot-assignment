package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.Instructor

data class SeminarForList(
    val id: Long,
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean,
    val instructors: List<Instructor>,
    val participantCount: Long? = 0
    )

package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.user.domain.Instructor

data class SearchSeminarResponse(
    val id: Long,
    val name: String,
    val instructors: List<Instructor>,
    val participantCount: Int,
)
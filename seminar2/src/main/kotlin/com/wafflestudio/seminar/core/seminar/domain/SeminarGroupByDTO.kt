package com.wafflestudio.seminar.core.seminar.domain

import com.querydsl.core.annotations.QueryProjection

class SeminarGroupByDTO @QueryProjection constructor(
    var id: Long?=0,
    var name: String?="",
) {
    var instructors: List<SeminarInstructorDTO>?=
        emptyList()
    var participantCount: Long?=0
}
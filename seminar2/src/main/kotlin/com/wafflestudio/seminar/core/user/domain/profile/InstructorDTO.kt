package com.wafflestudio.seminar.core.user.domain.profile

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.UserSeminar.domain.InstructorSeminarDTO

class InstructorDTO @QueryProjection constructor (
    var id: Long?,
    var company: String?,
    var year: Int?,
    var instructingSeminars: InstructorSeminarDTO ?= null
)
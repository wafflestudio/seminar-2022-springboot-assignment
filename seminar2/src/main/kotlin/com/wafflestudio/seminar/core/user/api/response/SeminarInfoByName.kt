package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.dto.seminar.TeacherDto

data class SeminarInfoByName(
    val id:Long?,
    val name: String?,
    val instructors: List<TeacherDto>?
)
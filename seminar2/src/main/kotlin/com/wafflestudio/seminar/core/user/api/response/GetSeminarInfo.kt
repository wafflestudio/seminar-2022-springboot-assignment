package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.dto.seminar.StudentDto
import com.wafflestudio.seminar.core.user.dto.seminar.TeacherDto

data class GetSeminarInfo(
    val id:Long?,  //seminarEntity
    val name: String?,  // seminarEntity
    val capacity: Int?,  // seminarEntity
    val count: Int?,  // seminarEntity
    val time: String?,  // seminarEntity
    val online: Boolean? = true,  // seminarEntity
    val instructors: List<TeacherDto>?,  // (User + UserSeminar)Entity
    val participants: List<StudentDto>?  // (User + UserSeminar)Entity
)
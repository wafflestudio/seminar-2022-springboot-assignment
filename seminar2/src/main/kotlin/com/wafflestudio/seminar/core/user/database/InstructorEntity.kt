package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
class InstructorEntity(
    val company: String,
    val year: Int?,
): BaseTimeEntity()
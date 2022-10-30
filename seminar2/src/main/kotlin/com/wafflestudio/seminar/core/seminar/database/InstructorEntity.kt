package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "instructor_profile")
class InstructorEntity(
    val userId: Long,
): BaseTimeEntity()
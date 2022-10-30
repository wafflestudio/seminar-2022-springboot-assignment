package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "instructor_profile")
class InstructorEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    val company: String,
    val year: Long?,
): BaseTimeEntity()
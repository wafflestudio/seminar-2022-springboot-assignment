package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.OneToOne

data class InstructorProfileEntity(
    @OneToOne
    val user: UserEntity,
    @Column(name = "company")
    val company: String? = "",
    @Column(name = "year")
    val year: Int? = null,
): BaseTimeEntity()
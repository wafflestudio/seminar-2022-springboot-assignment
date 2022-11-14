package com.wafflestudio.seminar.core.profile.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.*

@Entity
@Table(name = "instructor_profile")
class InstructorProfileEntity(
    @Column(nullable = false)
    var company: String,

    @Column(nullable = true)
    var year: Int? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
) : BaseTimeEntity()
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "instructorProfile")
class InstructorProfileEntity(
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: UserEntity,
    val company: String,
    val year: Int?,
) : BaseTimeEntity() 
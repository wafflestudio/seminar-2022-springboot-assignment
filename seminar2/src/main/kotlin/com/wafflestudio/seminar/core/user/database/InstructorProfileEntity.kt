package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "instructorProfile")
class InstructorProfileEntity(
    @OneToOne(mappedBy = "instructorProfile", cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: UserEntity? = null,
    var company: String,
    var year: Int?,
) : BaseTimeEntity() 
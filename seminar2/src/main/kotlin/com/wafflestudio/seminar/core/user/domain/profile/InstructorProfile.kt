package com.wafflestudio.seminar.core.user.domain.profile

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import javax.persistence.*
import javax.validation.constraints.PositiveOrZero

@Entity
class InstructorProfile (
    var company: String?= "",
    @field:PositiveOrZero
    var year: Int?= null,
    
    @OneToOne
    @JoinColumn(name="userId")
    var user: UserEntity
) : BaseTimeEntity()
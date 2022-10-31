package com.wafflestudio.seminar.core.user.domain.profile

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import javax.persistence.*

@Entity
class ParticipantProfile (
    var university: String?= "",
    val isRegistered: Boolean?= true,
    
    @OneToOne
    @JoinColumn(name="userId")
    var user: UserEntity,
) : BaseTimeEntity()
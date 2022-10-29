package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.*

@Entity
@Table(name = "userSeminar")
class UserSeminarEntity(
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity
) : BaseTimeEntity() 
package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class UserEntity(
    @Column(unique = true)
    val email: String,
    val username: String,
    val password: String,
): BaseTimeEntity()
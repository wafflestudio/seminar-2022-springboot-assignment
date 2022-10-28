package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val encodedPassword: String,
) : BaseTimeEntity() {
    fun toUser(): User {
        return User(
            email = email,
            username = username,
            encodedPassword = encodedPassword
        )
    }
}
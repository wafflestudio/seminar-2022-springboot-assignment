package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity (
    val nickname: String,
    val email: String,
    val encodedPassword: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L
    
    fun toUser(): User {
        return User(
            nickname = nickname,
            email = email,
            encodedPassword = encodedPassword,
            userId = userId
        )
    }
}
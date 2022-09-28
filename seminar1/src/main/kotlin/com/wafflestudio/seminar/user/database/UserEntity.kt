package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    
    val userID: String,
    val email: String,
    val password: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    fun toUser(): User {
        return User(
            userID = userID,
            email = email,
            password = password
        )
    }
}

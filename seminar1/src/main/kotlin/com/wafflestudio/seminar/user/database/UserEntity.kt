package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.ReadUserRequest
import com.wafflestudio.seminar.user.domain.User
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val userName: String,
    val email: String,
    var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    fun toUser(): User {
        return User(userName=userName, email=email, password=password)
    }
}
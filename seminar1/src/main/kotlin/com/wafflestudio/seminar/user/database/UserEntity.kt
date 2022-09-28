package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.ReadUserRequest
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
    
    fun toCreateUserDTO(): CreateUserRequest {
        return CreateUserRequest(nickname=userName, email=email, password=password)
    }
    
    fun toReadUserDTO(): ReadUserRequest {
        return ReadUserRequest(nickname = userName, email = email)
    }
}
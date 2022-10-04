package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.user.domain.CreateUserResponse
import com.wafflestudio.seminar.user.domain.LoginResponse
import com.wafflestudio.seminar.user.domain.UserResponse
import org.apache.juli.logging.Log
import javax.persistence.*

@Entity
@Table(name="users")
class UserEntity(

    @Column(nullable = false, name="name")
    val name : String,

    @Column(nullable = false, name="email", unique = true)
    val email : String,
    
    @Column(nullable = false, name="password")
    val password : String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0
    
    fun CreateUserResponse() : CreateUserResponse {
        return CreateUserResponse(userId, name, email)
    }
    
    fun LoginResponse() : LoginResponse {
        return LoginResponse(userId)
    }
    
    fun UserResponse() : UserResponse {
        return UserResponse(userId, name, email)
    }
}
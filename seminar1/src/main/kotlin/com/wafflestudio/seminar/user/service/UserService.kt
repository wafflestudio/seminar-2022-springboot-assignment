package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User401
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.request.ReadUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder
) {
    fun createUser(user: CreateUserRequest): CreateUserRequest {
        user.password = this.passwordEncoder.encode(user.password) 
        val user = userRepository.save(user.toEntity())
        
        return user.toCreateUserDTO()
    }
    
    fun loginUser(user: LoginUserRequest): Long {
        val existUser = userRepository.findByEmail(user.email)
        if (existUser == null) {
            throw User404("존재하지 않는 유저입니다")
        } else if (!passwordEncoder.matches(user.password, existUser.password)) {
            throw User401("비밀번호가 일치하지 않습니다")
        }
        return existUser.id
    }
    
    fun readUser(userId: Long): ReadUserRequest {
        val user: UserEntity? = userRepository.findByIdOrNull(userId)
        if (user == null) {
            throw User404("존재하지 않는 유저입니다")
        } else {
            return user.toReadUserDTO()
        }
    }
}
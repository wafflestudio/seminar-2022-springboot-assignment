package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.CreateUserResponse
import com.wafflestudio.seminar.user.api.response.LoginUserResponse
import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.exception.EmailDuplicatedException
import com.wafflestudio.seminar.user.exception.InvalidPasswordException
import com.wafflestudio.seminar.user.exception.UserNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
) {
    fun signup(createUserRequest: CreateUserRequest): CreateUserResponse {
        checkEmailDuplicate(createUserRequest.email)
        val savedUser = userRepository.save(createUserRequest.toUserEntity(passwordEncoder));
        return CreateUserResponse.from(savedUser)
    }

    fun login(loginUserRequest: LoginUserRequest): LoginUserResponse {
        val findUser = userRepository.findByEmail(loginUserRequest.email) 
            ?: throw UserNotFoundException()
        
        if (!passwordEncoder.matches(loginUserRequest.password ,findUser.encodedPassword))
            throw InvalidPasswordException()
        
        return LoginUserResponse(findUser.id)
    }

    fun findById(userId: Long): UserDetailResponse {
        val findUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }
        return UserDetailResponse.from(findUser)
    }

    private fun checkEmailDuplicate(email : String) {
        if (userRepository.findByEmail(email) != null) {
            throw EmailDuplicatedException()
        }
    }
    
}
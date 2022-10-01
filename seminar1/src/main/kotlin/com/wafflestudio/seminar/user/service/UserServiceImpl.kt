package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.user.api.*
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.UserLoginDTO
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
       private val userRepository: UserRepository,
       private val authConfig: AuthConfig
): UserService {
    override fun createUser(userRequest: CreateUserRequest) {
        // Assumes that blank (only whitespaces) input are not allowed.
        when {
            userRequest.nickname.isBlank() ->
                throw UserEmtpyNicknameException()
            
            userRequest.email.isBlank() ->
                throw UserEmtpyEmailException()
            
            userRequest.password.isBlank() ->
                throw UserEmtpyPasswordException()
            
            userRepository.existsByEmail(userRequest.email) ->
                throw UserNotUniqueEmailException()
        }
        
        val user = UserEntity(
                userRequest.nickname,
                userRequest.email,
                authConfig.passwordEncoder().encode(userRequest.password)
        )
        userRepository.save(user)
    }

    override fun login(userLogin: UserLoginDTO): String {
        val user: UserEntity = userRepository.findByEmail(userLogin.email)
                ?: throw LoginNoEmailException()
        
        if (!authConfig.passwordEncoder().matches(userLogin.password, user.password)) {
            throw LoginWrongPasswordException()
        }
        
        return user.nickname
    }
}
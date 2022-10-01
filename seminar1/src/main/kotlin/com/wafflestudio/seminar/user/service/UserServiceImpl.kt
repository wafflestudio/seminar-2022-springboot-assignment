package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.user.api.UserEmtpyEmailException
import com.wafflestudio.seminar.user.api.UserEmtpyNicknameException
import com.wafflestudio.seminar.user.api.UserEmtpyPasswordException
import com.wafflestudio.seminar.user.api.UserNotUniqueEmailException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
       private val userRepository: UserRepository 
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
                AuthConfig().passwordEncoder().encode(userRequest.password)
        )
        userRepository.save(user)
    }
}
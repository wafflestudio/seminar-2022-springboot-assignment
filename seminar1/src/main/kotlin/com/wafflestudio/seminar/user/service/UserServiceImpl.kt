package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.user.api.*
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.UserInfoDTO
import com.wafflestudio.seminar.user.api.request.UserLoginDTO
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
       private val userRepository: UserRepository,
       private val encoder: PasswordEncoder = AuthConfig().passwordEncoder()
): UserService {
    
    override fun createUser(userRequest: CreateUserRequest) {
        // Assumes that blank (only whitespaces) input are not allowed.
        when {
            userRequest.nickname.isBlank() ->
                throw CreateUserEmptyNicknameException()

            userRequest.email.isBlank() ->
                throw CreateUserEmptyEmailException()

            userRequest.password.isBlank() ->
                throw CreateUserEmptyPasswordException()

            userRepository.existsByEmail(userRequest.email) ->
                throw CreateUserNotUniqueEmailException()
        }
        
        val user = UserEntity(
                userRequest.nickname,
                userRequest.email,
                encoder.encode(userRequest.password)
        )
        userRepository.save(user)
    }

    override fun login(userLogin: UserLoginDTO): Long {
        val user: UserEntity = userRepository.findByEmail(userLogin.email)
                ?: throw LoginNoEmailException()
        
        if (!encoder.matches(userLogin.password, user.password)) {
            throw LoginWrongPasswordException()
        }
        
        return user.id
    }

    override fun getUserInfo(id: Long): UserInfoDTO {
        val user: UserEntity = userRepository.findByIdOrNull(id) ?: throw GetUserNotFindException()
        return UserInfoDTO(user.nickname, user.email)
    }
}
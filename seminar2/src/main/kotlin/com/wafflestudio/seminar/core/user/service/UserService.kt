package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.DuplicateEmailException
import com.wafflestudio.seminar.common.FailedToLogInException
import com.wafflestudio.seminar.common.UserNotFoundException
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.UserInfo
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): AuthToken
    fun logIn(logInRequest: LogInRequest): AuthToken
    fun getUserById(userid: Long): UserInfo
}

@Service
class UserServiceImpl(
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {

    override fun signUp(signUpRequest: SignUpRequest): AuthToken {
        val userEntity = try {
            userRepository.save(
                UserEntity(
                    signUpRequest.email,
                    signUpRequest.username,
                    passwordEncoder.encode(signUpRequest.password),
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw DuplicateEmailException
        }
        
        return authTokenService.generateTokenByUsername(userEntity.username, userEntity.id)
    }

    override fun logIn(logInRequest: LogInRequest): AuthToken {
        val userEntity = userRepository.findByEmail(logInRequest.email)
            ?: throw FailedToLogInException
        if (!passwordEncoder.matches(logInRequest.password, userEntity.password)) {
            throw FailedToLogInException
        }

        return authTokenService.generateTokenByUsername(userEntity.username, userEntity.id)
    }

    override fun getUserById(userid: Long): UserInfo {
        return userRepository.findByIdOrNull(userid)
            ?.let {
                UserInfo(
                    it.email,
                    it.username,
                    it.createdAt.toString(),
                    it.modifiedAt.toString(),
                )
            }
            ?: throw UserNotFoundException
    }

}
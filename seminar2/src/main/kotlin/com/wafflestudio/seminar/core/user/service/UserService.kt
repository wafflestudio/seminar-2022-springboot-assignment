package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.UserInfo
import com.wafflestudio.seminar.core.user.domain.UserRole
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): AuthToken
    fun logIn(logInRequest: LogInRequest): AuthToken
    fun getUserById(userid: Long): UserInfo
}

@Service
class UserServiceImpl(
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipateProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {

    @Transactional
    override fun signUp(signUpRequest: SignUpRequest): AuthToken {
        val userEntity = try {
            userRepository.save(
                signUpRequest.toUserEntity(passwordEncoder)
            )
        } catch (e: DataIntegrityViolationException) {
            throw DuplicateEmailException
        }

        when (signUpRequest.role) {
            UserRole.Participant ->
                userEntity.participantProfileEntity = participantProfileRepository.save(
                    signUpRequest.toParticipantProfileEntity()
                )
            UserRole.Instructor ->
                userEntity.instructorProfileEntity = instructorProfileRepository.save(
                    signUpRequest.toInstructorProfileEntry()
                )
        }

        return authTokenService.generateTokenByEmail(userEntity.email)
    }

    override fun logIn(logInRequest: LogInRequest): AuthToken {
        val userEntity = userRepository.findByEmail(logInRequest.email)
            ?: throw FailedToLogInException
        if (!passwordEncoder.matches(logInRequest.password, userEntity.password)) {
            throw FailedToLogInException
        }

        return authTokenService.generateTokenByEmail(userEntity.email)
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
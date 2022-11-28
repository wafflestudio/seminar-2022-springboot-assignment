package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.UserInfo
import com.wafflestudio.seminar.core.user.domain.UserRole
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun signUp(signUpRequest: SignUpRequest): AuthToken
    fun logIn(logInRequest: LogInRequest): AuthToken
    fun getUserById(user_id: Long): UserInfo
    fun updateUser(user_id: Long, updateRequest: UpdateRequest): UserInfo
    fun participantEnroll(user_id: Long, participantEnrollRequest: ParticipantEnrollRequest): UserInfo
}

@Service
class UserServiceImpl(
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {

    @Transactional
    override fun signUp(signUpRequest: SignUpRequest): AuthToken {
        val userEntity = signUpRequest.toUserEntity(passwordEncoder)

        when (signUpRequest.role) {
            UserRole.PARTICIPANT ->
                userEntity.participantProfile = participantProfileRepository.save(
                    signUpRequest.toParticipantProfileEntity()
                )
            UserRole.INSTRUCTOR ->
                userEntity.instructorProfile = instructorProfileRepository.save(
                    signUpRequest.toInstructorProfileEntry()
                )
        }
        
        try {
            userRepository.save(
                userEntity
            )
        } catch (e: DataIntegrityViolationException) {
            throw DuplicateEmailException
        }

        return authTokenService.generateTokenByEmail(userEntity.email)
    }

    override fun logIn(logInRequest: LogInRequest): AuthToken {
        val userEntity = userRepository.findByEmailWithAllProfile(logInRequest.email)
            ?: throw FailedToLogInException
        if (!passwordEncoder.matches(logInRequest.password, userEntity.password)) {
            throw FailedToLogInException
        }

        return authTokenService.generateTokenByEmail(userEntity.email)
    }

    @Transactional
    override fun getUserById(user_id: Long): UserInfo {
        return findUserWithAllInfo(user_id).toUserInfo()
    }

    @Transactional
    override fun updateUser(user_id: Long, updateRequest: UpdateRequest): UserInfo {
        val user = findUserWithAllInfo(user_id)
        
        return user.updateUser(updateRequest, passwordEncoder)
    }

    @Transactional
    override fun participantEnroll(user_id: Long, participantEnrollRequest: ParticipantEnrollRequest): UserInfo {
        val user = findUserWithAllInfo(user_id)
        if (user.participantProfile != null) {
            throw DuplicateParticipantEnrollmentException
        }
        
        user.participantProfile = participantProfileRepository.save(
            participantEnrollRequest.toParticipantProfileEntity()
        )
        
        return user.toUserInfo()
    }
    
    private fun findUserWithAllInfo(user_id: Long): UserEntity = userRepository.findUserWithAllInfo(user_id)
        ?: throw UserNotFoundException

}
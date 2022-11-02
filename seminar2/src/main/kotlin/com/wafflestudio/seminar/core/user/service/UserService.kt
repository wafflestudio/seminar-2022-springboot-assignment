package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.*
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
    fun getUserById(user_id: Long): UserInfo
    fun updateUser(user_id: Long, updateRequest: UpdateRequest): UserInfo
    fun participantEnroll(user_id: Long, participantEnrollRequest: ParticipantEnrollRequest): UserInfo
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
            UserRole.PARTICIPANT ->
                userEntity.participantProfile = participantProfileRepository.save(
                    signUpRequest.toParticipantProfileEntity()
                )
            UserRole.INSTRUCTOR ->
                userEntity.instructorProfile = instructorProfileRepository.save(
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

    override fun getUserById(user_id: Long): UserInfo {
        return findUser(user_id).toUserInfo()
    }

    @Transactional
    override fun updateUser(user_id: Long, updateRequest: UpdateRequest): UserInfo {
        val user = findUser(user_id)
        
        updateRequest.username?.let {
            user.username = it
        }
        updateRequest.password?.let {
            user.password = passwordEncoder.encode(it)
        }
        
        if (user.participantProfile != null) {
            updateRequest.university?.let {
                user.participantProfile!!.university = it
            }
        }
        
        if (user.instructorProfile != null) {
            updateRequest.company?.let {
                user.instructorProfile!!.company = it
            }
            updateRequest.year?.let {
                user.instructorProfile!!.year = it
            }
        }
        
        return user.toUserInfo()
    }

    @Transactional
    override fun participantEnroll(user_id: Long, participantEnrollRequest: ParticipantEnrollRequest): UserInfo {
        val user = findUser(user_id)
        if (user.participantProfile != null) {
            throw DuplicateParticipantEnrollmentException
        }
        
        user.participantProfile = participantProfileRepository.save(
            participantEnrollRequest.toParticipantProfileEntity()
        )
        
        return user.toUserInfo()
    }
    
    private fun findUser(user_id: Long): UserEntity = userRepository.findByIdOrNull(user_id)
        ?: throw UserNotFoundException

}
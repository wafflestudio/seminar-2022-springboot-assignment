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
    fun getUserById(userid: Long): UserInfo
    fun updateUser(userid: Long, updateRequest: UpdateRequest): UserInfo
    fun participantEnroll(userid: Long, participantEnrollRequest: ParticipantEnrollRequest): UserInfo
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
                userEntity.participantProfile = participantProfileRepository.save(
                    signUpRequest.toParticipantProfileEntity()
                )
            UserRole.Instructor ->
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

    override fun getUserById(userid: Long): UserInfo {
        return userRepository.findByIdOrNull(userid)
            ?.toUserInfo()
            ?: throw UserNotFoundException
    }

    @Transactional
    override fun updateUser(userid: Long, updateRequest: UpdateRequest): UserInfo {
        val user = userRepository.findByIdOrNull(userid) ?: throw UserNotFoundException
        
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
            } ?: throw NullNumberException
        }
        
        return user.toUserInfo()
    }

    @Transactional
    override fun participantEnroll(userid: Long, participantEnrollRequest: ParticipantEnrollRequest): UserInfo {
        val user = userRepository.findByIdOrNull(userid) ?: throw UserNotFoundException
        if (user.participantProfile != null) {
            throw DuplicateParticipantEnrollmentException
        }
        
        user.participantProfile = participantProfileRepository.save(
            participantEnrollRequest.toParticipantProfileEntity()
        )
        
        return user.toUserInfo()
    }

}
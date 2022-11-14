package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.config.AuthConfig
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.user.database.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

interface UserService {
    fun signUp(signUpRequest: UserDto.SignUpRequest): AuthToken
    fun logIn(email: String, password: String): AuthToken
    fun getMe(userId: Long): UserDto.UserResponse
    fun getProfile(userId: Long): UserDto.UserProfileResponse
    fun updateProfile(req: UserDto.UpdateRequest, userId: Long): UserDto.UserProfileResponse
    fun registerParticipant(req: UserDto.RegisterParticipantRequest, userId: Long): UserDto.UserProfileResponse
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val authConfig: AuthConfig,
    private val authTokenService: AuthTokenService,
    private val userRepositorySupport: UserRepositorySupport
) : UserService {

    @Transactional
    override fun signUp(req: UserDto.SignUpRequest): AuthToken {
        if (userRepository.existsByEmail(req.email!!)) {
            throw Seminar409("This email already exists.")
        }
        val userEntity = UserEntity(
            username = req.username!!,
            email = req.email,
            password = authConfig.passwordEncoder().encode(req.password)
        )
        when (req.role) {
            UserDto.Role.PARTICIPANT -> {
                userEntity.role = req.role
                userEntity.participantProfileEntity = ParticipantProfileEntity(
                    university = req.university ?: "",
                    isRegistered = req.isRegistered ?: true
                )
            }
            UserDto.Role.INSTRUCTOR -> {
                userEntity.role = req.role
                userEntity.instructorProfileEntity = InstructorProfileEntity(
                    company = req.company ?: "",
                    year = req.year
                )
            }
            else -> throw Seminar400("Role should be 'PARTICIPANT' or 'INSTRUCTOR'.")
        }
        userRepository.save(userEntity)
        return authTokenService.generateTokenByEmail(req.email)

    }

    @Transactional
    override fun logIn(email: String, password: String): AuthToken {
        val entity = userRepository.findByEmail(email) ?: throw Seminar404("This email doesn't exist.")
        if (!authConfig.passwordEncoder().matches(password, entity.password)) {
            throw Seminar401("Password is wrong.")
        }
        val modifiedToken: AuthToken = authTokenService.generateTokenByEmail(entity.email)
        entity.modifiedAt = LocalDateTime.now()
        return modifiedToken
    }

    @Transactional
    override fun getMe(userId: Long): UserDto.UserResponse {
        val userEntityOptional: Optional<UserEntity> = userRepository.findById(userId)
        val userEntity: UserEntity = userEntityOptional.get()
        return UserDto.UserResponse(
            id = userId,
            username = userEntity.username,
            email = userEntity.email,
            lastLogin = userEntity.modifiedAt!!.withNano(0)
        )
    }

    @Transactional
    override fun getProfile(userId: Long): UserDto.UserProfileResponse {
        if (!userRepository.existsById(userId)) {
            throw Seminar404("This userId doesn't exist.")
        }
        return userRepositorySupport.getProfile(userId)
    }

    @Transactional
    override fun updateProfile(req: UserDto.UpdateRequest, userId: Long): UserDto.UserProfileResponse {
        val userEntityOptional: Optional<UserEntity> = userRepository.findById(userId)
        val userEntity: UserEntity = userEntityOptional.get()
        val participantProfileEntity = userEntity.participantProfileEntity
        val instructorProfileEntity = userEntity.instructorProfileEntity
        if (participantProfileEntity != null) {
            participantProfileEntity.university = req.university ?: ""
            participantProfileEntity.modifiedAt = LocalDateTime.now()
            participantProfileRepository.save(participantProfileEntity)
        }
        if (instructorProfileEntity != null) {
            instructorProfileEntity.company = req.company ?: ""
            instructorProfileEntity.year = req.year
            instructorProfileEntity.modifiedAt = LocalDateTime.now()
            instructorProfileRepository.save(instructorProfileEntity)
        }
        if (!req.username.isNullOrBlank()) {
            userEntity.username = req.username
            userEntity.modifiedAt = LocalDateTime.now()
        }
        if (!req.password.isNullOrBlank()) {
            userEntity.password = req.password
            userEntity.modifiedAt = LocalDateTime.now()
        }
        userRepository.save(userEntity)
        return userRepositorySupport.getProfile(userId)
    }

    @Transactional
    override fun registerParticipant(
        req: UserDto.RegisterParticipantRequest,
        userId: Long
    ): UserDto.UserProfileResponse {
        val userEntityOptional: Optional<UserEntity> = userRepository.findById(userId)
        val userEntity: UserEntity = userEntityOptional.get()
        if (userEntity.participantProfileEntity != null) {
            throw Seminar409("You are already a participant.")
        }
        userEntity.participantProfileEntity = ParticipantProfileEntity(
            university = req.university ?: "",
            isRegistered = req.isRegistered ?: true
        )
        userEntity.modifiedAt = LocalDateTime.now()
        userRepository.save(userEntity)
        return userRepositorySupport.getProfile(userId)
    }

}
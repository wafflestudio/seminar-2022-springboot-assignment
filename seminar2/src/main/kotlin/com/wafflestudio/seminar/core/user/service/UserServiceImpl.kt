package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun signUp(signUpRequest: SignUpRequest): Long {
        if (userRepository.findByEmail(signUpRequest.email!!) != null) {
            throw Seminar409("Email is already in use")
        }
        val userEntity = signUpRequest.toUserEntity()
        userEntity.password = passwordEncoder.encode(userEntity.password)
        userEntity.lastLogin = LocalDateTime.now()
        userRepository.save(userEntity)
        return userEntity.id
    }

    override fun login(email: String, password: String) {
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("No existing user with email: ${email}")
        if (!passwordEncoder.matches(password, userEntity.password)) {
            throw Seminar401("Incorrect password")
        }
        userEntity.lastLogin = LocalDateTime.now()
    }

    @Transactional(readOnly = true)
    override fun getProfile(userId: Long): User {
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw Seminar404("No existing user with id: ${userId}")
        return userEntity.toDTO()
    }

    override fun editProfile(userId: Long, editProfileRequest: EditProfileRequest) {
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw Seminar404("No existing user with id: ${userId}")
        if (editProfileRequest.username != null) {
            userEntity.username = editProfileRequest.username
        }
        if (editProfileRequest.password != null) {
            userEntity.password = editProfileRequest.password
        }
        if (userEntity.participantProfile != null) {
            userEntity.participantProfile!!.university = editProfileRequest.university
        }
        if (userEntity.instructorProfile != null) {
            userEntity.instructorProfile!!.company = editProfileRequest.company
            userEntity.instructorProfile!!.year = editProfileRequest.year
        }
    }

    override fun registerParticipantProfile(userId: Long, participantRequest: ParticipantRequest) {
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw Seminar404("No existing user with id: ${userId}")
        if (userEntity.participantProfile != null) {
            throw Seminar409("You have participant profile already")
        }
        val participantProfileEntity =
            ParticipantProfileEntity(participantRequest.university, participantRequest.isRegistered)
        participantProfileEntity.addUser(userEntity)
        participantProfileRepository.save(participantProfileEntity)
    }
}
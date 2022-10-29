package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.LogExecutionTime
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.api.*
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val seminarRepository: SeminarRepository
) : UserService {
    
    @LogExecutionTime
    override fun createUser(signUpRequest: SignUpRequest): AuthToken {
        if (signUpRequest.email == null || signUpRequest.username == null || signUpRequest.password == null) {
            throw User400("필수 사항을 입력해주세요.")
        }
        val checkEmail = userRepository.findByEmail(signUpRequest.email)
        if (checkEmail != null) {
            throw User409("중복된 이메일이 존재합니다.")
        }

        val newPassword = this.passwordEncoder.encode(signUpRequest.password)

        if (signUpRequest.role == SignUpRequest.Role.INSTRUCTOR) {
            if (signUpRequest.year != null && signUpRequest.year < 0) {
                throw User400("year에 0 이상의 값을 입력해주세요.")
            }
            val instructorProfileEntity =
                InstructorProfileEntity(company = signUpRequest.company, year = signUpRequest.year)
            instructorProfileRepository.save(instructorProfileEntity)
            val newUserEntity = UserEntity(
                email = signUpRequest.email,
                username = signUpRequest.username,
                password = newPassword,
                instructorProfileEntity = instructorProfileEntity
            )
            userRepository.save(newUserEntity)
            return authTokenService.generateTokenByUsername(newUserEntity.email)
        } else {
            val participantProfileEntity = ParticipantProfileEntity(
                university = signUpRequest.university, isRegistered = signUpRequest.isRegistered
            )
            participantProfileRepository.save(participantProfileEntity)
            val newUserEntity = UserEntity(
                email = signUpRequest.email,
                username = signUpRequest.username,
                password = newPassword,
                participantProfileEntity = participantProfileEntity
            )
            userRepository.save(newUserEntity)
            return authTokenService.generateTokenByUsername(newUserEntity.email)
        }
    }
    
    @LogExecutionTime
    override fun loginUser(loginRequest: LoginRequest): AuthToken {
        if (loginRequest.email == null || loginRequest.password == null) {
            throw User400("필수 사항을 입력해주세요.")
        }
        val user: UserEntity = userRepository.findByEmail(loginRequest.email) ?: throw User404("존재하지 않는 이메일입니다.")
        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw User401("비밀번호가 일치하지 않습니다.")
        }
        user.modifiedAt = LocalDateTime.now()
        userRepository.save(user)
        return authTokenService.generateTokenByUsername(user.email)
    }
    
    @LogExecutionTime
    override fun getUser(authToken: String): String {
        return "인증에 성공하였습니다."
    }
    
    @LogExecutionTime
    override fun getProfile(userId: Long): User {
        val userInfo = userRepository.findByIdOrNull(userId) ?: throw User404("존재하지 않는 유저입니다.")
        if (userInfo.participantProfileEntity != null && userInfo.instructorProfileEntity != null) {
            val seminars = seminarRepository.findSeminarsByParticipantId(userId)
            val instructingSeminars = seminarRepository.findSeminarsByInstructorId(userId)
            return userInfo.toDTO(seminars, instructingSeminars)
        } else if (userInfo.participantProfileEntity == null) {
            val instructingSeminars = seminarRepository.findSeminarsByInstructorId(userId)
            return userInfo.toDTO(seminars = null, instructingSeminars)
        } else {
            val seminars = seminarRepository.findSeminarsByParticipantId(userId)
            return userInfo.toDTO(seminars, null)
        }
    }
    
    @LogExecutionTime
    override fun modifyProfile(authToken: String, modifyRequest: ModifyRequest): User {
        val userId = authTokenService.getCurrentUserId(authToken)
        val user = userRepository.findByIdOrNull(userId)
        if (modifyRequest.username != null) {
            user!!.username = modifyRequest.username
        }
        if (modifyRequest.password != null) {
            user!!.password = passwordEncoder.encode(modifyRequest.password)
        }
        if (user!!.instructorProfileEntity != null) {
            if (modifyRequest.company != null) {
                user.instructorProfileEntity!!.company = modifyRequest.company
            }
            if (modifyRequest.year != null) {
                user.instructorProfileEntity!!.year =
                    if (modifyRequest.year > 0) modifyRequest.year else throw User400("year에 0 이상의 값을 입력해주세요.")
            }
        } else {
            if (modifyRequest.university != null) {
                user.participantProfileEntity!!.university = modifyRequest.university
            }
        }
        userRepository.save(user)
        if (user.participantProfileEntity != null && user.instructorProfileEntity != null) {
            val seminars = seminarRepository.findSeminarsByParticipantId(userId)
            val instructingSeminars = seminarRepository.findSeminarsByInstructorId(userId)
            return user.toDTO(seminars, instructingSeminars)
        } else if (user.participantProfileEntity == null) {
            val instructingSeminars = seminarRepository.findSeminarsByInstructorId(userId)
            return user.toDTO(seminars = null, instructingSeminars)
        } else {
            val seminars = seminarRepository.findSeminarsByParticipantId(userId)
            return user.toDTO(seminars, null)
        }
    }

    @LogExecutionTime
    override fun addNewParticipant(authToken: String, registerParticipantRequest: RegisterParticipantRequest): User {
        val userId = authTokenService.getCurrentUserId(authToken)
        val user = userRepository.findByIdOrNull(userId)
        if (user!!.participantProfileEntity != null) {
            throw User400("이미 참여자로 등록되어있습니다.")
        }
        if (registerParticipantRequest.toBeParticipant) {
            val newParticipantProfile = ParticipantProfileEntity(university = registerParticipantRequest.university, isRegistered = registerParticipantRequest.isRegistered)
            participantProfileRepository.save(newParticipantProfile)
            user.participantProfileEntity = newParticipantProfile
            userRepository.save(user)
        }
        val seminars = seminarRepository.findSeminarsByParticipantId(userId)
        val instructingSeminars = seminarRepository.findSeminarsByInstructorId(userId)
        return user.toDTO(seminars, instructingSeminars)
    }
}
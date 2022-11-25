package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.mappingTable.ParticipantProfile
import com.wafflestudio.seminar.core.user.api.request.EditUserRequest
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipateRequest
import com.wafflestudio.seminar.exception.Seminar400
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.JwtResponse
import com.wafflestudio.seminar.core.user.api.response.UserResponse
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.exception.Seminar401
import com.wafflestudio.seminar.exception.Seminar404
import com.wafflestudio.seminar.exception.Seminar409
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private const val PARTICIPANT = "Participant"

@Service
@Transactional
class UserService (
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val passwordEncoder: PasswordEncoder
) {
    fun signUp(request: SignUpRequest): JwtResponse {
        isDuplicatedEmailAndUsername(request.email, request.username)
        val userEntity = createUserEntity(request, passwordEncoder)
        val savedUser = userRepository.save(userEntity)
        val authToken = authTokenService.generateTokenByUsername(savedUser.email)
        savedUser.lastLogin = savedUser.createdAt
        return JwtResponse(authToken.accessToken)
    }

    fun login(request: LoginRequest): JwtResponse {
        val findUser = userRepository.findByEmail(request.email) 
            ?: let { throw Seminar404("해당 email의 유저가 존재하지 않습니다.") }
        if (passwordEncoder.matches(request.password, findUser.password)) {
            val authToken = authTokenService.generateTokenByUsername(request.email)
            findUser.lastLogin = LocalDateTime.now()
            return JwtResponse(authToken.accessToken)
        }
        
        throw Seminar401("비밀번호가 틀렸습니다.")
    }

    fun findUserById(userId: Long): UserResponse {
        val findUser = userRepository.findById(userId)
            .orElseThrow { Seminar404("${userId}에 해당하는 id의 사용자가 존재하지 않습니다.") }
        return UserResponse.of(findUser)
    }

    fun editUserInfo(loginUser: UserEntity, request: EditUserRequest): UserResponse {
        val user = userRepository.findById(loginUser.id).get()
        user.username = request.username ?: user.username
        user.password = request.password ?: user.password
        user.participant?.university = request.university
        user.instructor?.company = request.company
        validateYear(request.year)
        user.instructor?.years = request.year
        return UserResponse.of(user)
    }

    fun participate(loginUser: UserEntity, request: ParticipateRequest): UserResponse {
        val user = userRepository.findById(loginUser.id).get()
        checkParticipated(user)
        user.participant = ParticipantProfile(request.university, request.isRegistered)
        return UserResponse.of(user)
    }

    private fun checkParticipated(user: UserEntity) {
        if (user.participant != null)
            throw Seminar409("이미 참여자인 사용자입니다.")
    }

    private fun validateYear(years: Int?) {
        if (years != null && years <= 0)
            throw Seminar400("year는 1 이상의 값이어야 합니다.")
    }

    private fun createUserEntity(request: SignUpRequest, passwordEncoder: PasswordEncoder): UserEntity {
        if (request.role == PARTICIPANT)
            return createParticipantUserEntity(request, passwordEncoder)
        return createInstructorUserEntity(request, passwordEncoder)
    }

    private fun createParticipantUserEntity(request: SignUpRequest, passwordEncoder: PasswordEncoder): UserEntity {
        return request.toParticipantUserEntity(request, passwordEncoder)
    }

    private fun createInstructorUserEntity(request: SignUpRequest, passwordEncoder: PasswordEncoder): UserEntity {
        return request.toInstructorUserEntity(request, passwordEncoder)
    }
    
    private fun isDuplicatedEmailAndUsername(email: String, username: String) {
        isDuplicateEmail(email)
        isDuplicateUsername(username)
    }

    private fun isDuplicateUsername(username: String) {
        userRepository.findByUsername(username) 
            ?.let { throw Seminar400("${username}는 중복된 username입니다.") }
    }

    private fun isDuplicateEmail(email: String) {
        userRepository.findByEmail(email) 
            ?.let { throw Seminar400("${email}은 중복된 email입니다.") }
    }

}
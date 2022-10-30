package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.profile.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.user.api.dto.UserResponse
import com.wafflestudio.seminar.core.user.api.dto.SignInRequest
import com.wafflestudio.seminar.core.user.api.dto.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface AuthService {
    fun createUserAndReturnToken(signUpRequest: SignUpRequest): AuthToken
    fun findUserAndReturnToken(signInRequest: SignInRequest): AuthToken
}

@Service
class AuthServiceImpl(
        private val userRepository: UserRepository,
        private val authTokenService: AuthTokenService,
        private val authConfig: AuthConfig,
        private val participantProfileRepository: ParticipantProfileRepository,
        private val instructorProfileRepository: InstructorProfileRepository,
) : AuthService {
    @Autowired
    private val encoder = authConfig.passwordEncoder()
    
    override fun createUserAndReturnToken(signUpRequest: SignUpRequest): AuthToken {
        try {
            val user = signUpRequest.saveAndGetUser(
                    encoder, 
                    userRepository, 
                    participantProfileRepository, 
                    instructorProfileRepository
            )
            return authTokenService.generateTokenByUsername(signUpRequest.email!!)
        } catch (e: DataIntegrityViolationException) {
            throw UserException409("Email already exists.")
        }
    }

    override fun findUserAndReturnToken(signInRequest: SignInRequest): AuthToken {
        // Search user using email
        var user = userRepository.findUserEntityByEmail(
                signInRequest.email!!
        )
        // Update lastLogin when not null and password is correct
        if (user == null || !encoder.matches(signInRequest.password!!, user.password)) {
            throw AuthException("Email or Password Wrong.")
        } else {
            // Update Last Login time
            user.lastLogin = LocalDateTime.now()
            user = userRepository.save(user)
            // Return generated token
            return authTokenService.generateTokenByUsername(user.email)
        }
    }
}
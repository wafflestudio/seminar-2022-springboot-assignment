package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.user.api.request.MeAuthenticationInfoResponse
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

interface AuthService {
    fun createUserAndReturnToken(signUpRequest: SignUpRequest): AuthToken
    fun findUserAndReturnToken(signInRequest: SignInRequest): AuthToken
    fun getUserAuthenticatedInfo(user: UserEntity): MeAuthenticationInfoResponse
}

@Service
class AuthServiceImpl(
        private val userRepository: UserRepository,
        private val authTokenService: AuthTokenService,
        private val authConfig: AuthConfig
) : AuthService {
    @Autowired
    private val encoder = authConfig.passwordEncoder()
    
    override fun createUserAndReturnToken(signUpRequest: SignUpRequest): AuthToken {
        try {
            userRepository.save(
                    UserEntity(
                            username = signUpRequest.username!!,
                            email = signUpRequest.email!!,
                            password = encoder.encode(signUpRequest.password!!)
                    )
            )
            return authTokenService.generateTokenByUsername(signUpRequest.email)
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

    override fun getUserAuthenticatedInfo(user: UserEntity): MeAuthenticationInfoResponse {
        return MeAuthenticationInfoResponse(
                message = "Authenticated!",
                username = user.username,
                email = user.email,
        )
    }
    
    // If not verified, 
//    override fun getUserAuthenticatedInfo(token: String): MeAuthenticationInfoResponse {
//        if (!authTokenService.verifyToken(token)) {
//            //FIXME
//            throw AuthException("User not Authenticated!")
////            throw ResponseStatusException(
////                HttpStatus.UNAUTHORIZED, 
////                "User not Authenticated!", 
////                AuthException()
////            )
//        }
////        val userId = authTokenService.getCurrentUserId(token)
////        val user = userRepository.findByIdOrNull(userId)
//        val user = authTokenService.getCurrentUser(token)
//                //FIXME: AuthException cannot be handled directly
//                ?: throw AuthException("User not Authenticated!")
////        ?: throw ResponseStatusException(
////                HttpStatus.UNAUTHORIZED, 
////                "User not Authenticated!", 
////                AuthException()
//        return MeAuthenticationInfoResponse(username = user.username, email = user.email)
////        )
//    }
}
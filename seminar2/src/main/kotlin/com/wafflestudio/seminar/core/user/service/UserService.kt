package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.api.request.EditUserRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.api.response.UserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.time.LocalDateTime

interface UserService {
    fun signUp(req: SignUpRequest) : AuthToken
    fun logIn(email: String, pwd: String) : AuthToken
    fun getUser(user_id: Long) : UserResponse
    fun editUser(req: EditUserRequest, user_id: Long) : UserResponse
    fun makeParticipant(req: ParticipantRequest, user_id: Long) : UserResponse
}


@Service
class UserServiceImpl(
    val authConfig: AuthConfig,
    val authTokenService: AuthTokenService,
    val userRepository: UserRepository,
    val userSeminarRepository: UserSeminarRepository
    ) : UserService {
    
    override fun signUp(req: SignUpRequest) : AuthToken {
        if(userRepository.findByEmail(req.email) != null) throw Seminar400("이미 가입된 사용자입니다.")
        if(req.year != null && req.year < 0) throw Seminar400("year 정보가 잘못되었습니다.")
        req.password = authConfig.passwordEncoder().encode(req.password)
        val user = req.toUserEntity()
        userRepository.save(user)
        return authTokenService.generateTokenByEmail(req.email)
    }

    override fun logIn(email: String, pwd: String) : AuthToken {
        val user = userRepository.findByEmail(email)?: throw Seminar404("해당 email의 사용자가 존재하지 않습니다.")
        if(authConfig.passwordEncoder().matches(pwd, user.password)) {
            user.lastLogin = LocalDateTime.now()
            userRepository.save(user)
            return authTokenService.generateTokenByEmail(email)
        }
        throw Seminar400("비밀번호가 틀렸습니다.")
    }

    override fun getUser(user_id: Long): UserResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("해당하는 id의 유저가 없습니다.")
        return user.toUserResponse(userSeminarRepository)
    }

    override fun editUser(req: EditUserRequest, user_id: Long): UserResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("해당하는 id의 유저가 없습니다.")
        user.edit(req, authConfig.passwordEncoder())
        userRepository.save(user)
        return user.toUserResponse(userSeminarRepository)
    }

    override fun makeParticipant(req: ParticipantRequest, user_id: Long): UserResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("해당하는 id의 유저가 없습니다.")
        user.makeParticipant(req)
        userRepository.save(user)
        return user.toUserResponse(userSeminarRepository)
    }
    
    
}
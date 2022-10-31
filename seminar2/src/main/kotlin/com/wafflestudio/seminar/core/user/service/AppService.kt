package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.api.request.EditRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.time.LocalDateTime

interface AppService {
    fun signUp(req: SignUpRequest) : AuthToken
    fun logIn(email: String, pwd: String) : AuthToken
}


@Service
class AppServiceImpl(
    val authConfig: AuthConfig,
    val authTokenService: AuthTokenService,
    val userRepository: UserRepository,
    val seminarRepository: SeminarRepository,
    val userSeminarRepository: UserSeminarRepository
    ) : AppService {
    
    override fun signUp(req: SignUpRequest) : AuthToken {
        val encPwd = authConfig.passwordEncoder().encode(req.password)
        if(userRepository.findByEmail(req.email) != null) throw NullPointerException()
        if(req.email == "" || req.username == "") throw NullPointerException()
        if(req.year != null && req.year <= 0) throw Seminar400("year 정보가 잘못되었습니다.")
        req.password = authConfig.passwordEncoder().encode(req.password)
        userRepository.save(req.toUserEntity())
        return authTokenService.generateTokenByEmail(req.email)
    }

    override fun logIn(email: String, pwd: String) : AuthToken {
        val user = userRepository.findByEmail(email)?: throw Seminar404("해당 email의 사용자가 존재하지 않습니다.")
        if(authConfig.passwordEncoder().matches(pwd, user.password)) {
            user.lastLogin = LocalDateTime.now()
            return authTokenService.generateTokenByEmail(email)
        }
        throw Seminar400("비밀번호가 틀렸습니다.")
    }
    
    
}
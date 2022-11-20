package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service;
import java.time.LocalDate
import javax.transaction.Transactional


@Service
class AuthService(
        private val userRepository: UserRepository,
        private val authTokenService: AuthTokenService,
        private val passwordEncoder: PasswordEncoder
) {
    fun signup(request: SignUpRequest): AuthToken {

        if (userRepository.findByEmail(request.email) != null) {
            throw Seminar400("해당 이메일은 이미 가입되어 있습니다")
        }
        
        val encodedPassword = this.passwordEncoder.encode(request.password)

        if(request.instructor != null){
            if (request.instructor.year != null && request.instructor.year <0 ) {
                throw Seminar400("연도에는 0 또는 양의 정수만 입력할 수 있습니다")
            }
        }
        
        val userEntity = UserEntity.of(request, encodedPassword)
        
        if(request.role == "PARTICIPANT" && request.participant != null){
            val participantProfileEntity = ParticipantProfileEntity.of(userEntity,request.participant)
            userEntity.participant = participantProfileEntity
        } else if(request.role == "INSTRUCTOR" && request.instructor != null){
            val instructorProfileEntity = InstructorProfileEntity.of(userEntity,request.instructor)
            userEntity.instructor = instructorProfileEntity
        } else Seminar400("진행자/참가자 체크 여부를 확인해주기 바랍니다")
        
        userRepository.save(userEntity)
        
        return authTokenService.generateTokenByEmail(request.email)
    }

    @Transactional
    fun login(request: LoginRequest): AuthToken {

        val userEntity = userRepository.findByEmail(request.email) ?: throw Seminar404("해당 이메일로 가입된 유저가 없습니다")
        
        if (this.passwordEncoder.matches(request.password, userEntity.password)) {
            val token = authTokenService.generateTokenByEmail(request.email)
            val lastLogin = LocalDate.from(authTokenService.getCurrentIssuedAt(token.accessToken))
            userEntity.lastLogin = lastLogin 
            return token
        } else {
            throw Seminar401("해당 비밀번호로 인증이 되지 않았습니다")
        }

    }

    



}

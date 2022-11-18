package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.user.dto.auth.InstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.auth.ParticipantProfileDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service;
import java.time.LocalDate



@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val passwordEncoder: PasswordEncoder
)  {
    fun signup(request: SignUpRequest): UserEntity {
        
        if(userRepository.findByEmail(request.email) != null) {
            throw Seminar400("해당 아이디로 가입할 수 없습니다")
        }
        return if(request.role == "PARTICIPANT"){
             val encodedPassword = this.passwordEncoder.encode(request.password)
             userRepository.save(signupParticipantEntity(request,encodedPassword))
            
            
        } else if(request.role == "INSTRUCTOR"){
            val encodedPassword = this.passwordEncoder.encode(request.password)
            if (request.instructor?.year != null) {
                
                if(request.instructor.year < 0){
                    throw Seminar400("연도에는 0 또는 양의 정수만 입력할 수 있습니다")
                }
            }
            userRepository.save(signupInstructorEntity(request, encodedPassword))
            
        } else throw Seminar400("오류")
    }
    
    fun login(request: LoginRequest): AuthToken {
        
        val userEntity = userRepository.findByEmail(request.email)
        if(this.passwordEncoder.matches(request.password, userEntity?.password)) {
            val token = authTokenService.generateTokenByEmail(request.email)
            val lastLogin = LocalDate.from(authTokenService.getCurrentIssuedAt(token.accessToken))
            loginEntity(request.email, lastLogin)
            return token
        } else {
            throw Seminar401("인증이 되지 않았습니다")
        }
        
    }
    
    val emptyMutableList = mutableListOf<UserSeminarEntity>()
    private fun signupParticipantEntity(user: SignUpRequest, encodedPassword: String) = user.run {
        UserEntity(username, email, encodedPassword, LocalDate.now(), null, ParticipantProfileEntity(participant), null, emptyMutableList)
    }
    
    private fun signupInstructorEntity(user: SignUpRequest, encodedPassword: String) = user.run {
        UserEntity(username, email, encodedPassword, LocalDate.now(), null, null, InstructorProfileEntity(instructor),emptyMutableList)

    }
    
    private fun loginEntity(email: String, lastLogin: LocalDate){
        val userEntity = userRepository.findByEmail(email)
        userEntity?.lastLogin = lastLogin
        if (userEntity != null) {
            userRepository.save(userEntity)
        }
    }
    
    private fun ParticipantProfileEntity(par: ParticipantProfileDto?) = par?.run {
        com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity(university, isRegistered)
    }
    
    private fun InstructorProfileEntity(ins: InstructorProfileDto?) = ins?.run {
        com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity(company, year)
    }
    

   
    
}

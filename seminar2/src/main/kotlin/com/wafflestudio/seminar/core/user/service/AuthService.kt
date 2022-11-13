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
    private val seminarRepository: SeminarRepository,
    private val passwordEncoder: PasswordEncoder
)  {
    fun signup(user: SignUpRequest): UserEntity {
        
        if(userRepository.findByEmail(user.email) != null) {
            throw Seminar400("해당 아이디로 가입할 수 없습니다")
        }
        return if(user.role == "PARTICIPANT"){
             val encodedPassword = this.passwordEncoder.encode(user.password)
             userRepository.save(signupParticipantEntity(user,encodedPassword))
            
            
        } else if(user.role == "INSTRUCTOR"){
            val encodedPassword = this.passwordEncoder.encode(user.password)
            if (user.instructor?.year != null) {
                
                if(user.instructor.year < 0){
                    throw Seminar400("0 또는 양의 정수만 입력할 수 있습니다")
                }
            }
            userRepository.save(signupInstructorEntity(user, encodedPassword))
            
        } else throw Seminar400("오류")
    }
    
    fun login(userLogin: LoginRequest): AuthToken {
        
        val userEntity = userRepository.findByEmail(userLogin.email)
        if(this.passwordEncoder.matches(userLogin.password, userEntity?.password)) {
            val token = authTokenService.generateTokenByEmail(userLogin.email)
            val lastLogin = LocalDate.from(authTokenService.getCurrentIssuedAt(token.accessToken))
            loginEntity(userLogin.email, lastLogin)
            return token
        } else {
            throw Seminar401("인증이 되지 않았습니다")
        }
        
    }
    
    val emptyMutableList1 = mutableListOf<UserSeminarEntity>()
    val emptyMutableList2 = mutableListOf<UserSeminarEntity>()
    private fun signupParticipantEntity(user: SignUpRequest, encodedPassword: String) = user.run {
        UserEntity(username, email, encodedPassword, LocalDate.now(), null, ParticipantProfileEntity(participant), null, emptyMutableList1)
    }
    
    private fun signupInstructorEntity(user: SignUpRequest, encodedPassword: String) = user.run {
        UserEntity(username, email, encodedPassword, LocalDate.now(), null, null, InstructorProfileEntity(instructor),emptyMutableList1)

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

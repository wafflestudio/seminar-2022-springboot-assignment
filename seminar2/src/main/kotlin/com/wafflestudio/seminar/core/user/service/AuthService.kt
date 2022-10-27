package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.dto.InstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.ParticipantProfileDto
import org.springframework.stereotype.Service;
import java.time.LocalDate



@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val seminarRepository: SeminarRepository
)  {
    fun signup(user: SignUpRequest): UserEntity {
        return if(user.role == "participant"){
             userRepository.save(signupParticipantEntity(user))
            
            
        } else if(user.role == "instructor"){
            userRepository.save(signupInstructorEntity(user))
            
        } else throw Seminar400("오류")
    }
    
    fun login(userLogin: LoginRequest): AuthToken {
        
        userRepository.findByEmail(userLogin.email)
        val token = authTokenService.generateTokenByEmail(userLogin.email)
        val lastLogin = LocalDate.from(authTokenService.getCurrentIssuedAt(token.accessToken))
        loginEntity(userLogin.email, lastLogin)
        return token
    }
    
    
    private fun signupParticipantEntity(user: SignUpRequest) = user.run {
        UserEntity(username, email, password, LocalDate.now(), null, ParticipantProfileEntity(email, participant), null)
    }
    
    private fun signupInstructorEntity(user: SignUpRequest) = user.run {
        UserEntity(username, email, password, LocalDate.now(), null, null, InstructorProfileEntity(email, instructor))

    }
    
    private fun loginEntity(email: String, lastLogin: LocalDate){
        val userEntity = userRepository.findByEmail(email)
        userEntity.lastLogin = lastLogin
        userRepository.save(userEntity)
    }
    
    private fun ParticipantProfileEntity(email: String, par: ParticipantProfileDto?) = par?.run {
        com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity(email, university, isRegistered)
    }
    
    private fun InstructorProfileEntity(email: String, ins: InstructorProfileDto?) = ins?.run {
        com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity(email, company, year)
    }
    

   
    
}

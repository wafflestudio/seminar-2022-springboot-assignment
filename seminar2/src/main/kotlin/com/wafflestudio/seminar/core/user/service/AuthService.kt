package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import com.wafflestudio.seminar.core.user.domain.UserSignup;
import com.wafflestudio.seminar.core.user.domain.UserLogin
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service;
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.Entity


@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val seminarRepository: SeminarRepository
)  {
    fun signup(user: UserSignup): UserEntity {
        return if(user.role == "participant"){
             userRepository.save(signupParticipantEntity(user))
            
            
        } else if(user.role == "instructor"){
            userRepository.save(signupInstructorEntity(user))
            
        } else throw Seminar400("오류")
    }
    
    fun login(userLogin: UserLogin): AuthToken {
        
        userRepository.findByEmail(userLogin.email)
        val token = authTokenService.generateTokenByEmail(userLogin.email)
        val lastLogin = LocalDate.from(authTokenService.getCurrentIssuedAt(token.accessToken))
        loginEntity(userLogin.email, lastLogin)
        return token
    }
    
    
    private fun signupParticipantEntity(user: UserSignup) = user.run {
        UserEntity(username, email, password, LocalDate.now(), null, ParticipantProfileEntity(email, participant), null)
    }
    
    private fun signupInstructorEntity(user: UserSignup) = user.run {
        UserEntity(username, email, password, LocalDate.now(), null, null, InstructorProfileEntity(email, instructor))

    }
    
    private fun loginEntity(email: String, lastLogin: LocalDate){
        val userEntity = userRepository.findByEmail(email)
        userEntity.lastLogin = lastLogin
        userRepository.save(userEntity)
    }
    
    private fun ParticipantProfileEntity(email: String, par: ParticipantProfile?) = par?.run {
        ParticipantProfileEntity(email, university,isRegistered)
    }
    
    private fun InstructorProfileEntity(email: String, ins: InstructorProfile?) = ins?.run { 
        InstructorProfileEntity(email, company, year)
    }
    

   
    
}

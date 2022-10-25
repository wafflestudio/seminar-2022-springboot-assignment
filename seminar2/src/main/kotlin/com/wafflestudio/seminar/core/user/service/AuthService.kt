package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import com.wafflestudio.seminar.core.user.domain.UserSignup;
import com.wafflestudio.seminar.core.user.domain.UserLogin
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service;
import javax.persistence.Entity


@Service
class AuthService(
    private val userRepository: UserRepository
)  {
    fun signup(user: UserSignup): UserEntity {
        return if(user.role == "participant"){
            println(1)
            userRepository.save(userParEntity(user))
        } else if(user.role == "instructor"){
            userRepository.save(userInsEntity(user))
            
        } else throw Seminar400("오류")
    }
    
    fun login(userLogin: UserLogin): UserEntity {
        return userRepository.findByEmail(userLogin.email)
    }
    
    
    private fun userParEntity(user: UserSignup) = user.run {
        UserEntity(username, email, password, dateJoined, ParticipantProfileEntity(user.email, participant), null)
    }
    
    private fun userInsEntity(user: UserSignup) = user.run {
        UserEntity(username, email, password, dateJoined, null, InstructorProfileEntity(user.email, instructor))

    }
    private fun ParticipantProfileEntity(email: String, par: ParticipantProfile?) = par?.run {
        ParticipantProfileEntity(email, university,isRegistered)
    }
    
    private fun InstructorProfileEntity(email: String, ins: InstructorProfile?) = ins?.run { 
        InstructorProfileEntity(email, company, year)
    }
    

   
    
}

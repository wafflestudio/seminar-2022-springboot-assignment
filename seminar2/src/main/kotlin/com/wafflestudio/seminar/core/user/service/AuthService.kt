package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.JpaRepository;
import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity;
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import com.wafflestudio.seminar.core.user.domain.UserSignup;
import com.wafflestudio.seminar.core.user.domain.UserLogin
import org.springframework.stereotype.Service;


@Service
class AuthService(
    private val jpaRepository:JpaRepository
)  {
    fun signup(user: UserSignup): UserEntity {
        return if(user.role == "participant"){
            println(1)
            jpaRepository.save(userParEntity(user))
        } else if(user.role == "instructor"){
            jpaRepository.save(userInsEntity(user))
        } else throw Seminar400("오류")
    }
    
    fun login(userLogin: UserLogin): UserEntity{
        return jpaRepository.findByEmail(userLogin.email)
    }
    
    
    private fun userParEntity(user: UserSignup) = user.run {
        UserEntity(username, email, password, dateJoined, ParticipantProfileEntity(participant), null)
    }
    
    private fun userInsEntity(user: UserSignup) = user.run {
        UserEntity(username, email, password, dateJoined, null, InstructorProfileEntity(instructor))

    }
    private fun ParticipantProfileEntity(par: ParticipantProfile?) = par?.run {
        ParticipantProfileEntity(university,isRegistered)
    }
    
    private fun InstructorProfileEntity(ins: InstructorProfile?) = ins?.run { 
        InstructorProfileEntity(company, year)
    }
    

   
    
}

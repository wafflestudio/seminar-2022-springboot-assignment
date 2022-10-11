package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.core.user.database.AuthRepository;
import com.wafflestudio.seminar.core.user.database.UserEntity;
import com.wafflestudio.seminar.core.user.domain.UserSignup;
import com.wafflestudio.seminar.core.user.domain.UserLogin
import org.springframework.stereotype.Service;


@Service
class AuthService(
    private val authRepository:AuthRepository
)  {
    fun signup(user: UserSignup): UserEntity {
        return authRepository.save(UserEntity(user.username, user.email, user.password,user.dateJoined))
    }
    
    fun login(userLogin: UserLogin): UserEntity{
        return authRepository.findByUsername(userLogin.username)
    }
    
    

   
    
}
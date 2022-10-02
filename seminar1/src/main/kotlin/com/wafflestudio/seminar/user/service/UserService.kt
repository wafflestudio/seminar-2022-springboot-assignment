package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInUserRequest
import com.wafflestudio.seminar.user.api.response.LoginInfo
import com.wafflestudio.seminar.user.api.response.userInfo
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.exception.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader


@Service
class UserService (
    val passwordEncoder: PasswordEncoder, 
    val userRepository: UserRepository
        ) {
    
    
   fun signUp(@RequestBody createUserRequest: CreateUserRequest): userInfo{
        
        if(userRepository.findByEmail(createUserRequest.email) != null){
            throw ExistsEmailException()
        }
        
        val user = userRepository.save(createUserRequest.toUser(passwordEncoder))
        return userInfo.toDTO(user)
    }

    fun signIn(@RequestBody signInUserRequest: SignInUserRequest): LoginInfo{
        val user = userRepository.findByEmail(signInUserRequest.email)
                    ?: throw NotExistsUsers()
        if(!checkPassword(user, signInUserRequest.password))
            throw IncorrectPasswordException()
        
        return LoginInfo.toDTO(user)
    }

    fun getUserInfo(@RequestHeader("X-User-ID") Id: Long): userInfo{
        val user = userRepository.findById(Id)
            .orElseThrow { 
                throw NotExistsUsers()
            }
        return userInfo.toDTO(user)
    }
    
    private fun checkPassword(user: UserEntity, password: String): Boolean{
        return passwordEncoder.matches(password, user.password)
    }
    
}
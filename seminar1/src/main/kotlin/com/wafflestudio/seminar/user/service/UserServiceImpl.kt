package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.BadRequestException
import com.wafflestudio.seminar.user.api.request.DuplicatedEmailException
import com.wafflestudio.seminar.user.api.request.UnAuthorizedException
import com.wafflestudio.seminar.user.api.request.UserNotFoundException
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder):UserService {
    override fun createUser(nickname: String,email: String,password: String) {
        val user = userRepository.findByEmail(email)
        if(user.isEmpty()){
            val encodedPassword = passwordEncoder.encode(password)
            userRepository.save(UserEntity(nickname,email,encodedPassword))
        }else{
            throw DuplicatedEmailException("Email is already in use")
        }
    }

    override fun login(email: String, password: String): Long {
        val user = userRepository.findByEmail(email)
        if(user.isNotEmpty()){
            if(passwordEncoder.matches(password,user[0].password)){
                return user[0].id
            }else{
                throw UnAuthorizedException("Incorrect password")
            }
        }else{
            throw UserNotFoundException("User not found")
        }
    }

    override fun getMyInfo(id: Long):UserEntity {
        val user = userRepository.findById(id)
        if(user.isEmpty()){
            throw BadRequestException("Bad request")
        }else{
            return user[0]
        }
    }

}
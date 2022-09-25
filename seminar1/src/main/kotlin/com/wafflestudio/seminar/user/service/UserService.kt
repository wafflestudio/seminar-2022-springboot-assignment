package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User1
import org.springframework.stereotype.Service

interface UserService {
    
    fun save(user: User1)
    fun findAll() : List<User1>?
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun save(user: User1) {
        userRepository.save(user)
    }
    
    override fun findAll() : List<User1>? {    
        return userRepository.findAll()
    }

}
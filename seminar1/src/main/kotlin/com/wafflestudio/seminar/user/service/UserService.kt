package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserPort
import org.springframework.stereotype.Service

interface UserService {
    fun getUser(id: Long): User
    fun register(name: String, email: String, pwd: String): String
    fun login(email: String, pwd: String) : Long
}

@Service
class UserServiceImpl(
    private val userPort: UserPort,
) : UserService {
    
    override fun getUser(id: Long): User {
        return userPort.getUser(id)
    }

    override fun register(name: String, email: String, pwd: String): String {
        return userPort.register(name, email, pwd)
    }
    
    override fun login(email: String, pwd: String): Long {
        return userPort.login(email, pwd)
    }
}
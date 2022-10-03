package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserPort
import org.springframework.stereotype.Service

interface UserService {
    fun getUser(id: Int): User
    fun register(name: String, email: String, password: String)
    fun login(email: String, password: String): Long
}

@Service
class UserServiceImpl(
    private val userPort: UserPort,
) : UserService {
    override fun getUser(id: Int): User {
        return userPort.getUser(id)
    }

    override fun register(name: String, email: String, password: String) {
        userPort.register(name, email, password)
    }

    override fun login(email: String, password: String): Long {
        return userPort.login(email, password)
    }


}
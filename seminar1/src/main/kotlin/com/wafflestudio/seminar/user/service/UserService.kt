package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface UserService {
    
    fun user(user: User)
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun user(user: User) {
        userRepository.save(user)
    }

}
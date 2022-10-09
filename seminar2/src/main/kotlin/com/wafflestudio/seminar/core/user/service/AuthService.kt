package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.core.user.database.AuthRepository;
import com.wafflestudio.seminar.core.user.database.UserEntity;
import com.wafflestudio.seminar.core.user.domain.User;
import org.springframework.stereotype.Service;

interface AuthService {

    fun save(user:UserEntity) : User




}

@Service
class AuthServiceImpl(
    private val authRepository:AuthRepository
) : AuthService {
    override fun save(user: UserEntity): User {
        val entity = authRepository.save(user)
        return User(entity)
    }

    private fun User(entity: UserEntity) = entity.run {
        User(
            id = id,
            username = username,
            email = email,
            password = password,
            lastLogin = lastLogin,
            dateJoined = dateJoined
        )
    }
    
}

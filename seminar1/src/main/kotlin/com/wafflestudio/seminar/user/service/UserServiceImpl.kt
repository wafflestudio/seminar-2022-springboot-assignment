package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.UserEmailAlreadyExistsException
import com.wafflestudio.seminar.user.api.UserNotAuthorizedException
import com.wafflestudio.seminar.user.api.UserNotFoundException
import com.wafflestudio.seminar.user.api.UserPasswordIncorrectException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {
    override fun getUser(id: Long?): User {
        if (id == null) throw UserNotAuthorizedException()
        
        val entity = userRepository.findById(id)
        if (entity.isEmpty) throw UserNotFoundException()
        return User(entity.get())
    }
    
    override fun createUser(user: CreateUserRequest): User {
        val entityByEmail = userRepository.findByEmail(user.email)
        if (entityByEmail.isPresent) throw UserEmailAlreadyExistsException()
        
        val newUser = UserEntity(
            nickname = user.nickname,
            email = user.email,
            password = user.password
        )
        val entity = userRepository.save(newUser)
        return User(entity)
    }
    
    override fun loginUser(user: LoginUserRequest): Long {
        val entity = userRepository.findByEmailAndPassword(user.email, user.password)
        if (entity.isEmpty) throw UserPasswordIncorrectException()
        return entity.get().id
    }

    private fun User(entity: UserEntity) = entity.run {
        User(id, nickname, email, password)
    }
}

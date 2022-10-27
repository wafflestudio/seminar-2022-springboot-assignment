package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.stereotype.Service

interface UserService {
    fun getUser(id: Long?): User
    fun createUser(user: SignUpRequest): User
    fun loginUser(user: SignInRequest): AuthToken
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
): UserService {
    override fun getUser(id: Long?): User {
        // TODO: error 구분해서 처리하기
        if (id == null) throw Error()
        val entity = userRepository.findById(id)
        if (entity.isEmpty) throw Error()
        
        return User(entity.get())
    }

    override fun createUser(user: SignUpRequest): User {
        val entityByEmail = userRepository.findByEmail(user.email)
        if (entityByEmail.isPresent) throw Error()

        val newUser = UserEntity(
            username = user.username,
            email = user.email,
            password = user.password
        )
        val entity = userRepository.save(newUser)
        return User(entity)
    }
    
    override fun loginUser(user: SignInRequest): AuthToken {
        println("first")
        val entity = userRepository.findByEmail(user.email)
        println(entity)
        if (entity.isEmpty) throw Error()
        if (entity.get().password != user.password) throw Error()
        println("second")
        println(entity.get().username)
        
        return authTokenService.generateTokenByUsername(entity.get().username)
    }
    
    private fun User(entity: UserEntity) = entity.run {
        User(id, username, email, password)
    }
}
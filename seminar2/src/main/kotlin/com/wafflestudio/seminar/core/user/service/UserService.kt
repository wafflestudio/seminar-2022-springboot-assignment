package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.stereotype.Service

interface UserService {
    fun getUser(id: Long?): User
    fun createUser(user: SignUpRequest): AuthToken
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

    override fun createUser(user: SignUpRequest): AuthToken {
        val entityByEmail = userRepository.findByEmail(user.email)
        if (entityByEmail.isPresent) throw Error()

        val newUser = UserEntity(
            username = user.username,
            email = user.email,
            password = user.password
        )
        val entity = userRepository.save(newUser)
        return authTokenService.generateTokenByUsername(entity.id)
    }
    
    override fun loginUser(user: SignInRequest): AuthToken {
        val entity = userRepository.findByEmail(user.email)
        if (entity.isEmpty) throw Seminar400("해당 email의 유저가 없습니다.")
        if (entity.get().password != user.password) throw Seminar400("비밀번호가 틀렸습니다.")
        
        return authTokenService.generateTokenByUsername(entity.get().id)
    }
    
    private fun User(entity: UserEntity) = entity.run {
        User(id, username, email, password)
    }
}
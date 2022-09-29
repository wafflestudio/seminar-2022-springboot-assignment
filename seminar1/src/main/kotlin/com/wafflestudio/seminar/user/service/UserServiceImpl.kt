package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User401
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserService {
    override fun createUser(user: CreateUserRequest): User {
        user.password = this.passwordEncoder.encode(user.password) 
        val entity:UserEntity = userRepository.save(user.toEntity())
        
        return User(entity)
    }
    
    override fun loginUser(user: LoginUserRequest): Long {
        val entity = userRepository.findByEmail(user.email) ?: throw User404("존재하지 않는 유저입니다")
        if (!passwordEncoder.matches(user.password, entity.password)) {
            throw User401("비밀번호가 일치하지 않습니다")
        }
        return entity.id
    }
    
    override fun readUser(userId: Long): User {
        val entity = userRepository.findByIdOrNull(userId) ?: throw User404("존재하지 않는 유저입니다")
        return User(entity)
    }
    
    private fun User(entity: UserEntity) = entity.run {
        User(userName, email, password)
    }
}
package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User401
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.api.User409
import com.wafflestudio.seminar.user.api.User500
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.dao.DataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    
    fun getUser(id: Long) : User {
        val user = userRepository.findById(id)
        if (user.isPresent) {
            return User.from(user.get())
        } else {
            throw User404("해당 user id를 찾을 수 없습니다 - status 404")
        }
    }
    
    @Transactional
    fun validateCreateUserRequest(request: CreateUserRequest) {
        val userEntity: Optional<UserEntity> = userRepository.findByEmail(request.email)
        if (userEntity.isPresent) {
            throw User409("중복된 email 주소입니다. 다른 email을 시도해주세요 - status 409")
        }
    }
    
    @Transactional
    fun createUser(request: CreateUserRequest) : User {
        validateCreateUserRequest(request)
        val userEntity: UserEntity = UserEntity(
            request.nickname,
            request.email,
            passwordEncoder.encode(request.password)
        )
        
        try {
            userRepository.save(userEntity)    
        } catch (e: DataAccessException) {
            throw User500("userRepository save error - 파악하지 못한 에러입니다 - status 500")
        }
        return User.from(userEntity)
    }
    
    fun login(request: LoginUserRequest): Map<String, Long> {
        val userEntity: Optional<UserEntity> = userRepository.findByEmail(request.email)
        if (userEntity.isEmpty) {
            throw User404("해당 user email을 찾을 수 없습니다 - status 404")
        }
        
        if (!passwordEncoder.matches(request.password, userEntity.get().password)) {
            throw User401("패스워드가 일치하지 않습니다 - status 401")
        }
        return mapOf<String, Long>("userId" to userEntity.get().id)
    }
    
    fun getLoginedUserMe(userId: Long): User {
        val userEntity: UserEntity? = userRepository.findByIdOrNull(userId)
        if (userEntity == null) {
            throw User404("해당 user id를 찾을 수 없습니다 - status 404")
        } else {
            return User.from(userEntity)
        }
    }
}
package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User401
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.api.User409
import com.wafflestudio.seminar.user.api.User500
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.dao.DataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    
    fun getUser(id: Long) : UserResponse {
        val user = userRepository.findById(id)
        if (user.isPresent) {
            return UserResponse.from(user.get())
        } else {
            throw User404("해당 user id를 찾을 수 없습니다 - status 404")
        }
    }
    
    @Transactional
    fun validateCreateUserRequest(request: CreateUserRequest) {
        val user: Optional<User> = userRepository.findByEmail(request.email)
        if (user.isPresent) {
            throw User409("중복된 email 주소입니다. 다른 email을 시도해주세요 - status 409")
        }
    }
    
    @Transactional
    fun createUser(request: CreateUserRequest) : UserResponse {
        validateCreateUserRequest(request)
        val user: User = User(
            request.nickname,
            request.email,
            passwordEncoder.encode(request.password)
        )
        
        try {
            userRepository.save(user)    
        } catch (e: DataAccessException) {
            throw User500("userRepository save error - 파악하지 못한 에러입니다 - status 500")
        }
        return UserResponse.from(user)
    }
    
    fun login(request: LoginUserRequest): Map<String, Long> {
        val user: Optional<User> = userRepository.findByEmail(request.email)
        if (user.isEmpty) {
            throw User404("해당 user email을 찾을 수 없습니다 - status 404")
        }
        
        if (!passwordEncoder.matches(request.password, user.get().password)) {
            throw User401("패스워드가 일치하지 않습니다 - status 401")
        }
        return mapOf<String, Long>("userId" to user.get().id)
    }
    
    fun getLoginedUserMe(userId: Long): UserResponse {
        val user: User? = userRepository.findByIdOrNull(userId)
        if (user == null) {
            throw User404("해당 user id를 찾을 수 없습니다 - status 404")
        } else {
            return UserResponse.from(user)
        }
    }
}
package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.*
import com.wafflestudio.seminar.user.api.request.*
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    override fun createUser(request: CreateUserRequest): User {
        val user = UserEntity(
            nickname = request.nickname,
            email = request.email,
            encodedPassword = passwordEncoder.encode(request.password)
        )
        try {
            userRepository.save(user)
            return User(user)
        } catch (e: DataIntegrityViolationException) {
            throw User409(request.email + "은(는) 이미 있는 이메일입니다")
        }
    }

    override fun login(request: LoginRequest): Long {
        try {
            val user = userRepository.findByEmail(request.email)
            if (!passwordEncoder.matches(request.password, user.encodedPassword)) {
                throw User401("비밀번호가 틀렸습니다")
            }
            return user.id
        } catch (e: EmptyResultDataAccessException) {
            throw User404(request.email + "은(는) 없는 이메일입니다")
        }
    }

    override fun getUser(xUserId: Long): User {
        val user = userRepository.findByIdOrNull(xUserId) ?: throw User404(xUserId.toString() + "은(는) 없는 아이디입니다")
        return User(user)
    }

    private fun User(entity: UserEntity) = entity.run {
        User(id, nickname, email)
    }
}
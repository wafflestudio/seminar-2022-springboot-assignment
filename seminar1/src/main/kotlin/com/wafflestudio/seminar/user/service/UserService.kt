package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User401
import com.wafflestudio.seminar.user.api.User403
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.api.User409
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.UserInfo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun addUser(createUserRequest: CreateUserRequest)
    fun login(loginRequest: LoginRequest): Long
    fun findUser(id: Long?): UserInfo
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun addUser(createUserRequest: CreateUserRequest) {
        if (userRepository.findByEmail(createUserRequest.email) != null) throw User409("Duplicated Email")
        val entity = UserEntity(
            createUserRequest.nickname,
            createUserRequest.email,
            passwordEncoder.encode(createUserRequest.password)
        )
        userRepository.save(entity)
    }

    override fun findUser(id: Long?): UserInfo {
        if (id == null) throw User403("Not Signed In")
        val entity = userRepository.findByIdOrNull(id) ?: throw User404("User Id Does Not Exist")
        return UserInfo(entity)
    }

    override fun login(loginRequest: LoginRequest): Long {
        val entity = userRepository.findByEmail(loginRequest.email) ?: throw User404("Email Does Not Exist")
        if (!passwordEncoder.matches(loginRequest.password, entity.password)) throw User401("Wrong Password")
        return entity.id

    }

    private fun UserInfo(entity: UserEntity) = entity.run {
        UserInfo(nickname, email)
    }
}
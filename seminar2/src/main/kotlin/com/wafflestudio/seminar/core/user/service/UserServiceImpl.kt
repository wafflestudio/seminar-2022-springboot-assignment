package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    @Transactional
    override fun register(signUpRequest: SignUpRequest): Long {
        val userEntity = signUpRequest.toUserEntity()
        userRepository.save(userEntity)
        return userEntity.id
    }

    @Transactional(readOnly = true)
    override fun findOne(userId: Long): User {
        val userEntity = userRepository.findById(userId).get()
        return userEntity.toDTO()
    }

    override fun update(): Long {
        TODO("Not yet implemented")
    }
}
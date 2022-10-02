package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.SeminarExceptionType
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.SignInResponse
import com.wafflestudio.seminar.user.domain.SignUpResponse
import com.wafflestudio.seminar.user.domain.User
import org.springframework.stereotype.Service

interface UserService {
    fun signUp(request: CreateUserRequest): SignUpResponse
    fun signIn(request: SignInRequest): SignInResponse
    fun getUserMe(id: Long?): User
}

@Service
class DefaultUserService(
    val repository: UserRepository
): UserService {
    override fun signUp(request: CreateUserRequest): SignUpResponse {
        when (repository.existsByEmail(request.email)) {
            true -> throw RuntimeException()
            false -> return repository.save(request.toDomain()).toSignUpResponse()
        }
    }

    override fun signIn(request: SignInRequest): SignInResponse {
        val user = repository.findByEmail(request.email) ?: throw java.lang.RuntimeException()
        when (user.password == request.password) {
            true -> return user.toSignInResponse()
            false -> throw java.lang.RuntimeException()
        } 
    }

    override fun getUserMe(id: Long?): User {
        val id = id ?: throw Seminar404(SeminarExceptionType.NotExistOSForId)
        val entity = repository.findById(id).orElseThrow() { throw java.lang.RuntimeException() }
        return User(entity)
    }

    private fun User(entity: UserEntity) = entity.run {
        User(
            userId = id,
            name = name,
            email = email
        )
    }
}
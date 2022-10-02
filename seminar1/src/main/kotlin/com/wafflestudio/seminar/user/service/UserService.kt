package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.*
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInRequest
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.SignInResponse
import com.wafflestudio.seminar.user.domain.SignUpResponse
import com.wafflestudio.seminar.user.domain.UserResponse
import com.wafflestudio.seminar.user.domain.UserSurveyResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun signUp(request: CreateUserRequest): SignUpResponse
    fun signIn(request: SignInRequest): SignInResponse
    fun getUserMe(id: Long?): UserResponse
    fun getUserMeSurvey(id: Long?): UserSurveyResponse
}

@Service
class DefaultUserService(
    val repository: UserRepository
): UserService {
    @Transactional
    override fun signUp(request: CreateUserRequest): SignUpResponse {
        when (repository.existsByEmail(request.email)) {
            true -> throw User409(UserExceptionType.ExistUserEmail)
            false -> {
                
                return repository.save(request.toEntity()).toSignUpResponse()
            } 
        }
    }
    
    @Transactional
    override fun signIn(request: SignInRequest): SignInResponse {
        val user = repository.findByEmail(request.email) ?: throw java.lang.RuntimeException()
        when (user.password == request.password) {
            true -> return user.toSignInResponse()
            false -> throw java.lang.RuntimeException()
        } 
    }
    
    @Transactional
    override fun getUserMe(id: Long?): UserResponse {
        val id = id ?: throw User403(UserExceptionType.NeedsAuthetication)
        val entity = repository.findById(id).orElseThrow() { throw User404(UserExceptionType.NotExistUserId) }
        return entity.toUserResponse()
    }

    @Transactional
    override fun getUserMeSurvey(id: Long?): UserSurveyResponse {
        val id = id ?: throw User403(UserExceptionType.NeedsAuthetication)
        val entity = repository.findById(id).orElseThrow() { throw User404(UserExceptionType.NotExistUserId) }
        entity.surveyResponse ?: throw User404(UserExceptionType.NotExistSurvey)
        return entity.toUserSurveyResponse()
    }
}
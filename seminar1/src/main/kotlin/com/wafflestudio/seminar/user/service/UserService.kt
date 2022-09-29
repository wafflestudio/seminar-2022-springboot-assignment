package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
)
{
    //@Transactional(propagation = , isolation = ,noRollbackFor = ,readOnly = ,rollbackFor = ,timeout = )/
    @Transactional 
    fun saveUser(request: CreateUserRequest){ //UserCreateRequest should be implemented
        val newUser = User(request.nickname, request.email)
        userRepository.save(newUser)  // since we extend JPA repository, no need to implement save method myself
    }
    //getUsers using UserResponse
    
}
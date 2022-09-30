package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
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
        val newUser = UserEntity(request.nickname, request.email,request.password,request.survey)
        userRepository.save(newUser)  // since we extend JPA repository, no need to implement save method myself
    }
    @Transactional(readOnly = true)
    fun getUser(entity: UserEntity)=entity.run{
        UserEntity(nickname, email, password, survey)
    }
    
    //getUsers using UserResponse
    
}
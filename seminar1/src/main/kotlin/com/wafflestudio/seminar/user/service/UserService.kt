package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User401
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.api.User409
import com.wafflestudio.seminar.user.api.UserException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.SignInUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    fun createUser(
        createUserRequest: CreateUserRequest
    ): String
    
    fun signInUser(
        signInUserRequest: SignInUserRequest
    ): Long
    
    fun getUserInfo(
        id: Long
    ): User
}


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserService {
    override fun createUser(
        createUserRequest: CreateUserRequest
    ): String{
        val userEntity: UserEntity = UserEntity(
            userName = createUserRequest.userName,
            email = createUserRequest.email,
            password = passwordEncoder.encode(createUserRequest.password)
        )
        if (userRepository.findByEmail(createUserRequest.email) != null){
            throw User409("사용할 수 없는 email 입니다.")
        }
        userRepository.save(userEntity)
        return "계정이 성공적으로 생성되었습니다."
        }

    override fun signInUser(
        signInUserRequest: SignInUserRequest
    ): Long {
        val userEntity = userRepository.findByEmail(signInUserRequest.email)
        if (userEntity == null){
            throw User404("존재하지 않는 이메일입니다.")
        } else if (passwordEncoder.matches(signInUserRequest.password,userEntity.password)){
            return userEntity.id
        } else{
            throw User401("이메일과 비밀번호가 일치하지 않습니다.")
        }
        
    }

    override fun getUserInfo(
        id: Long
    ): User {
        val userEntity: UserEntity? = userRepository.findByIdOrNull(id)
        if (userEntity == null){
            throw User404("존재하지 않는 아이디입니다.")
        } else{
            return User.to(userEntity)
        }
    }
}
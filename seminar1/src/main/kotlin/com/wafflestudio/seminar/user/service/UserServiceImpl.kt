package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.DuplicateEmailException
import com.wafflestudio.seminar.user.api.InvalidPasswordException
import com.wafflestudio.seminar.user.api.UserNotFoundException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): UserService {
    override fun createUser(createUserRequest: CreateUserRequest): User {
        val userEntity = UserEntity(
            createUserRequest.nickname, 
            createUserRequest.email,
            passwordEncoder.encode(createUserRequest.password)
        )
        try{
            userRepository.save(userEntity)
            return userEntity.toUser()    
        } catch(e: DataIntegrityViolationException){
            throw DuplicateEmailException("입력하신 이메일 주소는 이미 사용중입니다.")
        }
    }

    override fun login(loginRequest: LoginRequest): Long {
        val userEntity = userRepository.findByEmail(loginRequest.email) ?: throw UserNotFoundException("등록되지 않은 이메일입니다.")
        if(!passwordEncoder.matches(loginRequest.password, userEntity.encodedPassword)){
            throw InvalidPasswordException("비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요.")
        }
        return userEntity.userId
    }

    override fun getUserInfo(xUserId: Long): User {
        val userEntity = userRepository.findByIdOrNull(xUserId) ?: throw UserNotFoundException("등록되지 않은 아이디입니다.")
        return userEntity.toUser()
    }
}
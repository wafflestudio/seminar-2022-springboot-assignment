package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.DiffPasswordException
import com.wafflestudio.seminar.user.api.SameEmailException
import com.wafflestudio.seminar.user.api.UserNotExistException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.UserResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder
) {
    fun createUser(req: CreateUserRequest): String {
        if (userRepository.findByEmail(req.email) != null) 
          throw SameEmailException("동일한 email이 있습니다.")
        userRepository.save(req.convertToUser(passwordEncoder))
        return req.nickname + " 님의 회원가입이 완료되었습니다."
    }
    
    fun login(req: LoginUserRequest): Long {
        val user = userRepository.findByEmail(req.email) ?: throw UserNotExistException("해당 이메일이 존재하지 않습니다.")
        if(!passwordEncoder.matches(req.password, user.password))
            throw DiffPasswordException("비밀번호를 잘못 입력했습니다.")
        return user.userId
    }
    
    fun getUser(id: Long): UserResponse {
        val user = userRepository.findByUserId(id) ?: throw UserNotExistException("존재하지 않는 유저입니다.")
        return UserResponse(user.userId, user.nickname, user.email)
    }
}
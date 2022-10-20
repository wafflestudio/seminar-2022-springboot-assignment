package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.*
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.stereotype.Service
import com.wafflestudio.seminar.user.api.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class UserService(val userRepository: UserRepository,
                    val passwordEncoder: PasswordEncoder
                  ) {
    fun makeUser(user: CreateUserRequest): String {
        if(user.nickname=="") throw Seminar400("닉네임을 입력해주세요.")
        if(user.email=="") throw Seminar400("이메일을 입력해주세요.")
        val encPwd = passwordEncoder.encode(user.password)
        if ((userRepository.findByEmail(user.email)) != null) {
            throw Seminar409("${user.email}: 중복된 이메일입니다.")
        }
        val userEntity = UserEntity(user.nickname, user.email, encPwd)
        userRepository.save(userEntity)
        return "사용자 등록이 완료되었습니다."
    }
    fun loginUser(email: String, pwd: String): Long? {
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("이메일이 잘못되었습니다.")
        if(passwordEncoder.matches(pwd, userEntity.encPwd)) return userEntity.id
        else throw Seminar401("비밀번호가 잘못되었습니다.")
    }
    fun getUser(userId: Long?): User {
        if(userId == null) throw Seminar403("유저 번호를 입력해주세요.")
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw Seminar404("존재하지 않는 유저입니다.")
        return userEntity.run{
            User(userId, name, email, encPwd)
        }
    }
}
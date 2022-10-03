package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder
) {
    fun register(user: CreateUserRequest) : String{
        
        if (user.email.isBlank() || user.nickname.isBlank()) throw Seminar400("이름과 이메일을 모두 작성해주세요.")
        if (userRepository.findByEmail(user.email) != null) throw Seminar409("이미 존재하는 이메일입니다.")
        
        val encodedPW: String = passwordEncoder.encode(user.password)
        userRepository.save(UserEntity(user.nickname, user.email, encodedPW))
        
        return "회원가입에 성공하였습니다."
    }
    
    fun login(user: LoginUserRequest) : String{
        
        val entity = userRepository.findByEmail(user.email) ?: throw Seminar404("존재하지 않는 계정입니다.")
        if (!(passwordEncoder.matches(user.password, entity.password))) throw Seminar401("비밀번호가 틀렸습니다.")
        
        return "로그인에 성공하였습니다. (id: ${entity.id})"
    }
    
    fun getUser(id: Long) : String{
        
        val entity = userRepository.findByIdOrNull(id) ?: throw Seminar404("존재하지 않는 계정입니다.")
        
        return "id: ${entity.id} 님의 유저 정보입니다. \n\n email: ${entity.email} \n nickname: ${entity.nickname}"
    }
    
}



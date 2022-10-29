package com.wafflestudio.seminar.lecture.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.exception.Seminar401
import com.wafflestudio.seminar.exception.Seminar404
import com.wafflestudio.seminar.exception.Seminar409
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
//    private val userPort: UserPort
    @Autowired
    private val userRepository: UserRepository,
    
) {
    private val passwordEncoder = AuthConfig().passwordEncoder()

    @Transactional
    fun register(name : String, email : String, password: String) {
        if(userRepository.findByEmail(email) != null) {
            throw(Seminar409("이미 가입된 이메일 주소입니다"))
        }else{
            val newUser = UserEntity(name, email, encodePassword(password))
            userRepository.save(newUser)
        }
    }
    @Transactional
    fun findById(id: Long) : UserEntity{
        return  userRepository.findByUserId(id) ?: throw Seminar404("존재하지 않는 id입니다.")
    }
    @Transactional
    fun findByEmail(email: String) : UserEntity
    {
        return userRepository.findByEmail(email)?:throw Seminar404("존재하지 않는 이메일 주소입니다.")
    }
    @Transactional
    fun login(email: String, password: String) : Long?
    {
        val user : UserEntity = userRepository.findByEmail(email) ?: throw Seminar404("존재하지 않는 이메일 주소입니다.")
        if(passwordEncoder.matches(password, user.password))
        {
            return user.userId
        }else{
            throw Seminar401("비밀번호를 확인해주세요.")
        }
    }

    private fun encodePassword(raw: String) : String
    {
        return passwordEncoder.encode(raw)
    }
}
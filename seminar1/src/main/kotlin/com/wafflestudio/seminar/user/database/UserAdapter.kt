package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.exception.Seminar400
import com.wafflestudio.seminar.exception.Seminar401
import com.wafflestudio.seminar.exception.Seminar404
import com.wafflestudio.seminar.exception.Seminar409
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserPort

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val authConfig: AuthConfig
) : UserPort {
    
    
    @Transactional
    override fun register(name: String, email: String, password: String) {
        if (name == "" || email == "") {
            throw Seminar400("이름/이메일을 모두 입력해주세요.")
        } else if (userRepository.findByEmail(email) != null) {
            throw Seminar409("이미 존재하는 이메일입니다.")
        } else {
            userRepository.save(UserEntity(name, email, authConfig.passwordEncoder().encode(password)))
        }
    }

    @Transactional
    override fun login(email: String, password: String): Long {
        val entity = userRepository.findByEmail(email)
        if (entity == null) {
            throw Seminar404("존재하지 않는 이메일입니다.")
        } else {
            if (authConfig.passwordEncoder().matches(password, entity.password)) {
                return entity.id
            } else {
                throw Seminar401("비밀번호가 틀립니다.")
            }
        }
    }

    @Transactional
    override fun getUser(id: Int): User {
        val entity = userRepository.findByIdOrNull(id.toLong()) ?: throw Seminar404("존재하지 않는 유저입니다.")
        return entity.toUser()
    }
    
    
}
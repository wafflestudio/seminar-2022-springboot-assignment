package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.exception.ErrorCode
import com.wafflestudio.seminar.exception.SeminarException
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val authConfig: AuthConfig,
) : UserPort {
    
    @Transactional
    override fun register(name: String, email: String, pwd: String): String {
        with(userRepository) {
            if (this.existsByEmail(email)) throw SeminarException(ErrorCode.EMAIL_CONFLICT)
            else this.save(UserEntity(name, email, authConfig.passwordEncoder().encode(pwd)))
        }
        return "성공적으로 유저 정보가 등록되었습니다."
    }

    override fun login(email: String, pwd: String): Long {
        val entity = userRepository.findByEmail(email)
            ?: throw SeminarException(ErrorCode.USER_NOT_FOUND)

        return if (authConfig.passwordEncoder().matches(pwd, entity.password)) entity.id
            else throw SeminarException(ErrorCode.NOT_AUTHORIZED)       
    }

    override fun getUser(id: Long): User {
        val entity = userRepository.findByIdOrNull(id)
            ?: throw SeminarException(ErrorCode.USER_NOT_FOUND)
        return entity.toUser()
    }
    
}
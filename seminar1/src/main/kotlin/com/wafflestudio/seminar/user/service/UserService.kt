package com.wafflestudio.seminar.user.service


import com.wafflestudio.seminar.user.api.request.Seminar401
import com.wafflestudio.seminar.user.api.request.Seminar404
import com.wafflestudio.seminar.user.api.request.Seminar409
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.UserLogin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService{
    fun register(user: UserEntity): String
    fun login(user: UserLogin): Long
    fun findMe(userId: Long): UserEntity?
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserService {
    override fun register(user: UserEntity): String {
        if(userRepository.findByEmail(user.email) == null){
            userRepository.save(UserEntity(user.nickname, user.email, passwordEncoder.encode(user.password)))
            return "유저가 생성되었습니다."
        }else{
            throw Seminar409("중복된 email 주소 입니다.")
        }
    }

    override fun login(user: UserLogin): Long {
        val userEntity = userRepository.findByEmail(user.email)
        if(userEntity == null) {
            throw Seminar404("존재하지 않는 email 주소입니다.")
        }else{
            if(passwordEncoder.matches(user.password, userEntity.password)){
                return userEntity.id
            }else{
                throw Seminar401("비밀번호가 틀렸습니다.")
            }
        }
    }

    override fun findMe(userId: Long): UserEntity? {
        return userRepository.findByIdOrNull(userId) ?: throw Seminar404("존재하지 않는 유저입니다.")
    }

}
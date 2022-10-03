package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.domain.UserEntity
import com.wafflestudio.seminar.user.dto.CreateUserDTO
import com.wafflestudio.seminar.user.dto.LoginUserDTO
import com.wafflestudio.seminar.user.exception.*
import com.wafflestudio.seminar.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) {
    fun createUser(user: CreateUserDTO) : UserEntity {
        if (userRepository.findByEmail(user.email) != null) throw User409("이메일이 중복됩니다.")
        
        val encodedPW: String = passwordEncoder.encode(user.password)
        return userRepository.save(UserEntity(user.nickname, user.email, encodedPW))
    }

    fun login(loginUser: LoginUserDTO) : Long{
        val user = userRepository.findByEmail(loginUser.email) ?: throw User404("존재하지 않는 계정입니다.")
        if (!(passwordEncoder.matches(loginUser.password, user.password))) throw User401("비밀번호가 다릅니다.")

        return user.id
    }

    fun getUser(id: Long) : UserEntity{
        return userRepository.findByIdOrNull(id) ?: throw User404("존재하지 않는 계정입니다.")
    }

}
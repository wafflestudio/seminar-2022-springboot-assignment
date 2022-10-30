package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateUserRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
  private val authTokenService: AuthTokenService,
  private val passwordEncoder: PasswordEncoder,
) {
  @Transactional
  fun createUser(request: SignUpRequest): AuthToken {
    val encodedPwd = passwordEncoder.encode(request.password)
    val user = userRepository.save(request.toUserEntity(encodedPwd))
    return authTokenService.generateTokenByEmail(user.email)
  }
  
  @Transactional
  fun logIn(email: String, password: String): AuthToken {
    val user = userRepository.findByEmail(email) 
      ?: throw Seminar404("먼저 회원가입 해주세요.")
    
    if (!passwordEncoder.matches(password, user.password)) {
      throw Seminar401("비밀번호가 올바르지 않습니다.")
    }
    
    user.updateLastLogIn()    
    return authTokenService.generateTokenByEmail(email)
  }
  
  @Transactional
  fun update(userId: Long, request: UpdateUserRequest): User = request.run {
    val user = userRepository.findByIdOrNull(userId)!!
    user.update(
      username = username,
      encodedPwd = password?.let { passwordEncoder.encode(it) },
      university = university,
      company = company,
      year = year,
    )    
    
    User.of(user)
  }
  
  fun getUser(userId: Long): User {
    val entity = userRepository.findByIdOrNull(userId) ?: throw Seminar404("")
    return User.of(entity)
  }
  
  @Transactional
  fun registerParticipant(userId: Long, request: RegisterParticipantRequest): User {
    val user = userRepository.findByIdOrNull(userId)!!
    user.createProfile(request.university, request.isRegistered)
    
    return User.of(userRepository.save(user))
  }
}
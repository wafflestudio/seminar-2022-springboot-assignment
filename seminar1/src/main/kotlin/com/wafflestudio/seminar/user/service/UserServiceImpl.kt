package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.LocalDateTime
import javax.transaction.Transactional


@Service
class UserServiceImpl(private val userRepository: UserRepository,private val osRepository: OsRepository,private val surveyResponseRepository: SurveyResponseRepository,private val passwordEncoder: PasswordEncoder): UserService {
    
    @Transactional
    override fun create(user: CreateUserRequest): User {
        validateDuplicateMember(user)
        val encodePassword = passwordEncoder.encode(user.password)
        val userEntity = UserEntity(user.nickname, user.email, encodePassword)
        val entity = userRepository.save(userEntity)
        return User(entity)
    }
    
    private fun validateDuplicateMember(user: CreateUserRequest){
        if(userRepository.findByEmail(user.email) == null) { return}
        else {throw Seminar409("이미 존재하는 이메일입니다.")}
    }

    override fun login(userinfo: LoginUserRequest): User {
        val account = userRepository.findByEmail(userinfo.email) ?: throw Seminar401("존재하지 않는 이메일입니다.")
        if(passwordEncoder.matches(userinfo.password, account.password)) {
            return User(account)
        } else {
            throw Seminar401("틀린 비밀번호입니다.")
        }
    }

    override fun inquiry(id: Long?): UserEntity? {
        return userRepository.findByIdOrNull(id) ?: throw Seminar404("존재하지 않는 유저입니다.")
    }

    private fun User(entity: UserEntity) = entity.run {
        User(
            id = id,
            nickname = nickname,
            email = email,
            password = password
        )
    }

        override fun surveyCreate(id: Long, request: CreateSurveyRequest): Long? {
        val user = userRepository.findByIdOrNull(id) ?: throw Seminar404("회원가입을 진행해주세요.")
        val os = osRepository.findByOsName(request.operatingSystem) ?: throw Seminar404("해당되는 OS가 존재하지 않습니다.")
        val timestamp: LocalDateTime = LocalDateTime.now()
        surveyResponseRepository.save(
            SurveyResponseEntity(
            os,
            request.springExp!!,
            request.rdbExp!!,
            request.programmingExp!!,
            request.major,
            request.grade,
            timestamp,
            request.backendReason,
            request.waffleReason,
            request.somethingToSay,
            user
        )
        )
        return 1
    }
    
}
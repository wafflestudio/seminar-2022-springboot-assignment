package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
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
    val osRepository: OsRepository,
    val surveyResponseRepository: SurveyResponseRepository,
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

    fun postSurvey(id:Long, surveyResponseEntity: SurveyResponseEntity): SurveyResponseEntity{
        osRepository.findByOsName(surveyResponseEntity.operatingSystem.osName) ?: throw Seminar404("OS ID가 없습니다.")
        val user = userRepository.findByIdOrNull(id) ?: throw User404("존재하지 않는 계정입니다.")
        
        return surveyResponseRepository.save(SurveyResponseEntity(
            operatingSystem = surveyResponseEntity.operatingSystem,
            springExp = surveyResponseEntity.springExp,
            rdbExp = surveyResponseEntity.rdbExp,
            programmingExp = surveyResponseEntity.programmingExp,
            user = user))
    }
}
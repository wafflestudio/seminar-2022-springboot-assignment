package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.*
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.UserLogin
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserSurvey
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    
    fun save(user: UserEntity) : User
    
    fun findAll() : List<UserEntity>?
    fun findByEmailAndPassword(user: UserLogin): User
    fun findByEmail(email: String): UserEntity?
    fun checkUser(value: Long?): UserEntity?
    fun survey(survey: UserSurvey, value: Long?): SurveyResponseEntity
    
    
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun save(user: UserEntity): User {
        
        if (findByEmail(user.email) == null){
            user.password = this.passwordEncoder.encode(user.password)
            val entity = userRepository.save(user)
            return User(entity)
        }
        
        else {
            throw Seminar409("이메일이 중복되었습니다")
        }
       
    }
    
    override fun findAll() : List<UserEntity>? {    
        return userRepository.findAll()
    }

    override fun findByEmail(email: String): UserEntity? {
        return userRepository.findByEmail(email)
    }
    
    override fun findByEmailAndPassword(user: UserLogin): User {
        val validateEmail = findByEmail(user.email) ?: throw Seminar401("해당하는 이메일이 존재하지 않습니다")
        val passwordEncode = findByEmail(user.email)?.password
        if(this.passwordEncoder.matches(user.password, passwordEncode)){
            val entity = userRepository.findByEmailAndPassword(user.email, passwordEncode)
            return User(entity)
           
        } else {
            throw Seminar401("비밀번호가 틀렸습니다")
        }
        
        
    }

    
    
    override fun checkUser(value: Long?): UserEntity? {
        
        return userRepository.findByIdOrNull(value) ?: throw Seminar404("존재하지 않는 유저입니다")
    }


    override fun survey(survey: UserSurvey, value: Long?): SurveyResponseEntity {
        val operatingSystem = osRepository.findByOsName(survey.os) ?: throw Seminar404("os 이름이 잘못 입력되었습니다.")
        val surveyResponse =
            SurveyResponseEntity(
            operatingSystem = operatingSystem,
            springExp = survey.springExp,
            rdbExp = survey.rdbExp,
            programmingExp = survey.programmingExp, 
            major = survey.major,
            grade = survey.grade, 
            timestamp = survey.timestamp,
            backendReason = survey.backendReason, 
            waffleReason = survey.waffleReason, 
            somethingToSay = survey.somethingToSay, 
            user = userRepository.findByIdOrNull(value)
        )
        surveyResponseRepository.save(surveyResponse)
        return surveyResponse
    }
    
    
    private fun User(entity: UserEntity) = entity.run {
        User(
            id = id,
            nickname = nickname,
            email = email, 
            password = password
        )
    }


    
}
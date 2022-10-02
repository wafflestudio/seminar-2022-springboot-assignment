package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.request.*
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val passwordEncoder: PasswordEncoder
    ):UserService {
    
    override fun createUser(nickname: String,email: String,password: String) {
        val user = userRepository.findByEmail(email)
        if(user==null){
            val encodedPassword = passwordEncoder.encode(password)
            userRepository.save(UserEntity(nickname,email,encodedPassword))
        }else{
            throw DuplicatedEmailException("Email is already in use")
        }
    }

    override fun login(email: String, password: String): Long {
        val user = userRepository.findByEmail(email)
        if(user!=null){
            if(passwordEncoder.matches(password,user.password)){
                return user.id
            }else{
                throw UnAuthorizedException("Incorrect Password")
            }
        }else{
            throw NotFoundException("User Not Found")
        }
    }

    override fun getMyInfo(id: Long):UserEntity {
        val user = userRepository.findById(id)
        if(user==null){
            throw NotFoundException("User Not Found")
        }else{
            return user
        }
    }

    override fun survey(surveyRequest: SurveyRequest, userId:Long) {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User Not Found")
        if(surveyRequest.programming_exp==0 || surveyRequest.rdb_exp==0 || surveyRequest.spring_exp==0 || surveyRequest.os==null){
            throw BadRequestException("spring_exp, rdb_exp, programming_exp, os are required")
        }
        var osId = 0L;
        osId = when(surveyRequest.os){
            "MacOS" -> 1L
            "Linux" -> 2L
            "Windows" -> 3L
            else -> throw NotFoundException("OS Not Found")
        }
        surveyResponseRepository.save(SurveyResponseEntity(
            timestamp = LocalDateTime.now(),
            operatingSystem = osRepository.findById(osId).get(),
            springExp = surveyRequest.spring_exp,
            rdbExp = surveyRequest.rdb_exp,
            programmingExp = surveyRequest.programming_exp,
            major = surveyRequest.major,
            grade = surveyRequest.grade,
            backendReason = surveyRequest.backendReason,
            waffleReason = surveyRequest.waffleReason,
            somethingToSay = surveyRequest.somethingToSay,
            user = user
        ))
        
    }

}
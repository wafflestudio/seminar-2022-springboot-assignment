package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.CreateUserRequest
import com.wafflestudio.seminar.user.api.LoginRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.CreateUserResponse
import com.wafflestudio.seminar.user.domain.LoginResponse
import com.wafflestudio.seminar.user.domain.UserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.Exception
import java.time.LocalDateTime

interface UserService {
    fun CreateUser(request: CreateUserRequest) : CreateUserResponse
    fun Login(request: LoginRequest) : LoginResponse
    fun GetUser(id: Long) : UserResponse
    fun GetUserSurvey(id: Long, request: CreateSurveyRequest)
}

@Service
class UserServiceImpl(
    private val userRepository : UserRepository,
    private val passwordEncoder: PasswordEncoder, 
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository
) : UserService {
    override fun CreateUser(request: CreateUserRequest): CreateUserResponse {
        if(request.name=="" || request.email=="") {
            throw Seminar400("이메일과 이름을 입력해 주세요.")
        }
        if(userRepository.findByEmail(request.email)!=null) {
            throw Seminar409("중복된 이메일입니다.")
        }
        else {
            val t = passwordEncoder.encode(request.password)
            val p = UserEntity(request.name, request.email, t)
            userRepository.save(p)
            return p.CreateUserResponse()
        }
    }

    override fun Login(request: LoginRequest): LoginResponse {
        if(userRepository.findByEmail(request.email)!=null) {
            val user = userRepository.findByEmail(request.email)
            if(user != null && passwordEncoder.matches(request.password, user.password)) {
                return user.LoginResponse()
            }
            else {
                throw Seminar401("비밀번호가 일치하지 않습니다.")
            }
        }
        else {
            throw Seminar404("등록되지 않은 이메일입니다.")
        }
    }

    override fun GetUser(id: Long): UserResponse {
        val user = userRepository.findByIdOrNull(id)
        if(user==null) {
            throw Seminar404("존재하지 않는 유저입니다.")
        }
        else {
            return user.UserResponse()
        }
    }

    override fun GetUserSurvey(id: Long, request: CreateSurveyRequest) {
        if(request.springExp==null || request.rdbExp==null || request.programmingExp==null || request.operatingSystem=="") {
            throw Seminar400("정보를 입력해주세요.")
        }
        else {
            val os=osRepository.findByOsName(request.operatingSystem!!)
            if(os==null) {
                throw Seminar404("Operating System이 존재하지 않습니다.")
            }
            else {
                val user = userRepository.findByIdOrNull(id)
                if(user==null) {
                    throw Seminar404("존재하지 않는 유저입니다.")
                }
                else {
                    val t = SurveyResponseEntity(
                        os,
                        request.springExp,
                        request.rdbExp,
                        request.programmingExp,
                        request.major,
                        request.grade,
                        LocalDateTime.now(),
                        request.backendReason,
                        request.waffleReason,
                        request.somethingToSay,
                        user
                    )
                    surveyResponseRepository.save(t)
                }
            }
        }
    }
}
package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.SeminarUserException
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface UserService {
    fun UserRegister(
        userRequest: CreateUserRequest,
        authConfig: AuthConfig
    ): String?
    fun UserLogin(
        values: String,
        authConfig: AuthConfig
    ): Long?
    fun UserGetInfo(
        values: String?,
        authConfig: AuthConfig
    ): String?
    fun UserSurveyResponse(
        values: String,
        data: CreateSurveyRequest,
        authConfig: AuthConfig
    )
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
): UserService {
    
    override fun UserRegister(
        userRequest: CreateUserRequest,
        authConfig: AuthConfig
    ): String? {
        if (userRepository.findByEmail(userRequest.email) != null) {
            throw SeminarUserException("The same email is already registered.", HttpStatus.CONFLICT)
        } else {
            val passwordEncoder = authConfig.passwordEncoder()
            val user = UserEntity(userRequest.nickname, userRequest.email, passwordEncoder.encode(userRequest.password)) //passwordEncoder.encode(userRequest.password))
            userRepository.save(user)
            return "Register for email : "+userRequest.email
        }
    }
    
    
    override fun UserLogin(
        values: String,
        authConfig: AuthConfig
    ): Long? {
        val passwordEncoder = authConfig.passwordEncoder()
        val email = values.split(",")[0]
        val password = values.split(",")[1]
        val user = userRepository.findByEmail(email)
        if (user == null) {
            throw SeminarUserException("Wrong email for login.", HttpStatus.NOT_FOUND)
        } else if (!passwordEncoder.matches(password, user.password)) {
            throw SeminarUserException("Wrong password for login.", HttpStatus.UNAUTHORIZED)
        } else {
            return user.id
        }
    }
    
    
    override fun UserGetInfo(values: String?, authConfig: AuthConfig): String? {
        if (values == null) {
            throw SeminarUserException("Need custom header", HttpStatus.FORBIDDEN)
        } else {
            val user = userRepository.findByEmail(values)
            if (user == null){
                throw SeminarUserException("Wrong email for search info.", HttpStatus.NOT_FOUND)
            }
            return ""+user.id+","+user.name+","+user.email
            //"ID : "+user.id+", Nickname : "+user.name+", Email : "+user.email
        }
    }
    
    
    override fun UserSurveyResponse(
        values: String,
        data: CreateSurveyRequest,
        authConfig: AuthConfig
    ) {
        if (data == null) {
            throw SeminarUserException("Need more info.", HttpStatus.BAD_REQUEST)
        } else if (osRepository.findByOsName(data.osName) == null) {
            throw SeminarUserException("Wrong OS.", HttpStatus.NOT_FOUND)
        }
        val dataForSave = SurveyResponseEntity(
            userID = UserLogin(values, authConfig),
            operatingSystem = osRepository.findByOsName(data.osName)!!,
            springExp = data.springExp,
            rdbExp = data.rdbExp,
            programmingExp = data.programmingExp,
            major = data.major!!,
            grade = data.grade!!,
            timestamp = LocalDateTime.now(),
            backendReason = data.backendReason,
            waffleReason = data.waffleReason,
            somethingToSay = data.somethingToSay,
        )
        surveyResponseRepository.save(dataForSave)
    }
}
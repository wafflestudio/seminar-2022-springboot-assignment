package com.wafflestudio.seminar.user.service


import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.DoLoginRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface UserService {
    fun doLogin(req: DoLoginRequest):Long
    fun getUser(userId:Long): User
    fun saveUser(req: CreateUserRequest)
    fun createSurveyResponse(createSurveyRequest: CreateSurveyRequest, userId: Long):SurveyResponseEntity
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
    private val pwEncoder: PasswordEncoder,
): UserService {
    override fun doLogin(req: DoLoginRequest):Long {
        val user = userRepository.findByEmail(req.email) ?: throw Seminar400("Email or password is not correct.")
        if (pwEncoder.matches(req.pw, user.pw)) return user.id
        else throw Seminar401("Email or password is not correct")
    }
    override fun getUser(userId:Long): User {
        val user =  userRepository.findByIdOrNull(userId) ?: throw Seminar400("User does not exist.")
        return user.toUser()
    }
        
    override fun saveUser(req: CreateUserRequest){
        if(userRepository.findByEmail(req.email)!=null) throw Seminar409("This is a registered email.")
        userRepository.save(UserEntity(req.nickname,req.email, pwEncoder.encode(req.password)))
    }


    override fun createSurveyResponse(req: CreateSurveyRequest, userId: Long):SurveyResponseEntity {
        return surveyResponseRepository.save(
            SurveyResponseEntity(
            operatingSystem = osRepository.findByOsName(req.os) ?: throw Seminar400("This OS doesn't exist."),
            springExp =  req.spring_exp ?: throw Seminar400("Spring_exp must exist."),
            rdbExp = req.rdb_exp ?: throw Seminar400("rdb_exp must exist."),
            programmingExp = req.programming_exp ?: throw Seminar400("programming_exp must exist."),
            major = req.major,
            grade = req.grade,
            timestamp = LocalDateTime.now(),
            backendReason = req.backendReason,
            waffleReason = req.waffleReason,
            somethingToSay = req.somethingToSay,
            user = userRepository.findById(userId).get() //?: throw Seminar404("This User doesn't exist")
        )
        )
    }
}
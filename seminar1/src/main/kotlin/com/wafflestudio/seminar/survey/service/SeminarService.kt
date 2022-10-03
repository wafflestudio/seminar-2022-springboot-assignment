package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun createSurvey(createSurveyRequest: CreateSurveyRequest, userId: Long)
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun createUser(createUserRequest: CreateUserRequest)
    fun loginUser(loginUserRequest: LoginUserRequest): Long
    fun user(userId: Long): User
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : SeminarService {
    override fun os(id: Long): OperatingSystem {
        val entity = osRepository.findByIdOrNull(id) ?: throw Seminar404("OS를 찾을 수 없어요.")
        return OperatingSystem(entity)
    }

    override fun os(name: String): OperatingSystem {
        val entity = osRepository.findByOsName(name) ?: throw Seminar404("OS ${name}을 찾을 수 없어요.")
        return OperatingSystem(entity)
    }

    override fun createSurvey(createSurveyRequest: CreateSurveyRequest, userId: Long) {
        val operatingSystemEntity = osRepository.findByOsName(createSurveyRequest.os!!)
            ?: throw Seminar404("${createSurveyRequest.os}: 서버에 존재하지 않는 OS입니다.")
        val surveyResponseEntity = createSurveyRequest.run {
            SurveyResponseEntity(
                operatingSystemEntity,
                springExp!!,
                rdbExp!!,
                programmingExp!!,
                major!!,
                grade!!,
                LocalDateTime.now(),
                backendReason,
                waffleReason,
                somethingToSay,
                userId
            )
        }
        surveyResponseRepository.save(surveyResponseEntity)
    }

    override fun surveyResponseList(): List<SurveyResponse> {
        val surveyEntityList = surveyResponseRepository.findAll()
        return surveyEntityList.map(::SurveyResponse)
    }

    override fun surveyResponse(id: Long): SurveyResponse {
        val surveyEntity = surveyResponseRepository.findByIdOrNull(id) ?: throw Seminar404("설문 결과를 찾을 수 없어요.")
        return SurveyResponse(surveyEntity)
    }

    override fun createUser(createUserRequest: CreateUserRequest) {
        val nickname = createUserRequest.nickname!!
        val email = createUserRequest.email!!
        val encodedPassword = passwordEncoder.encode(createUserRequest.password)

        userRepository.findByEmail(email)?.let { throw Seminar409("${email}: 중복된 이메일입니다.") }

        val userEntity = UserEntity(nickname, email, encodedPassword)
        userRepository.save(userEntity)
    }

    override fun loginUser(loginUserRequest: LoginUserRequest): Long {
        val email = loginUserRequest.email!!
        val password = loginUserRequest.password!!
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("해당 이메일(${email})로 등록된 사용자가 없어요.")

        return if (passwordEncoder.matches(
                password, userEntity.encodedPassword
            )
        ) userEntity.id else throw Seminar401("비밀번호가 잘못되었습니다.")
    }

    override fun user(userId: Long): User {
        val userEntity =
            userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당 아이디(ID: ${userId})로 등록된 사용자가 없어요.")
        return User(userEntity)
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, des)
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        SurveyResponse(id = id,
            operatingSystem = OperatingSystem(operatingSystem),
            springExp = springExp,
            rdbExp = rdbExp,
            programmingExp = programmingExp,
            major = major,
            grade = grade,
            timestamp = timestamp,
            backendReason = backendReason,
            waffleReason = waffleReason,
            somethingToSay = somethingToSay,
            user = if (userId == null) {
                null
            } else {
                userRepository.findByIdOrNull(userId)?.run { User(id, nickname, email, encodedPassword) }
            })
    }

    private fun User(entity: UserEntity) = entity.run {
        User(id, nickname, email, encodedPassword)
    }
}
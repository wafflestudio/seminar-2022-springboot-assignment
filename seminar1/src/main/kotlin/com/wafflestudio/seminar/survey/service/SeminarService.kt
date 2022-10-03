package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun createSurvey(user_id: Long, surveyRequest: CreateSurveyRequest): SurveyResponse?
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
    private val userRepository: UserRepository
) : SeminarService {
    override fun os(id: Long): OperatingSystem {
        val entity = osRepository.findByIdOrNull(id) ?: throw Seminar404("OS를 찾을 수 없어요.")
        return OperatingSystem(entity)
    }

    override fun os(name: String): OperatingSystem {
        val entity = osRepository.findByOsName(name) ?: throw Seminar404("OS ${name}을 찾을 수 없어요.")
        return OperatingSystem(entity)
    }

    override fun surveyResponseList(): List<SurveyResponse> {
        val surveyEntityList = surveyResponseRepository.findAll()
        return surveyEntityList.map(::SurveyResponse)
    }

    override fun surveyResponse(id: Long): SurveyResponse {
        val surveyEntity = surveyResponseRepository.findByIdOrNull(id) ?: throw Seminar404("설문 결과를 찾을 수 없어요.")
        return SurveyResponse(surveyEntity)
    }

    override fun createSurvey(user_id: Long, surveyRequest: CreateSurveyRequest): SurveyResponse? {
        val os=osRepository.findByOsName(surveyRequest.osName)?:throw Seminar404("존재하지 않는 os 입니다. ")
        val user=userRepository.findByIdOrNull(user_id)?: throw Seminar404("존재하지 않는 id 입니다. ")
        val survey = 
                    SurveyResponseEntity(
                        operatingSystem = osRepository.findByOsName(surveyRequest.osName)!!,
                        springExp = surveyRequest.springExp,
                        rdbExp = surveyRequest.rdbExp,
                        programmingExp = surveyRequest.programmingExp,
                        major = surveyRequest.major!!,
                        grade = surveyRequest.grade!!,
                        timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                        backendReason = surveyRequest.backendReason,
                        waffleReason = surveyRequest.waffleReason,
                        somethingToSay = surveyRequest.somethingToSay,
                        user = user
                    )

        
        surveyResponseRepository.save(survey)
        return SurveyResponse(survey)
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        SurveyResponse(
            id = id,
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
            user= user?.run {User(nickname, "123", email)}
        )
    }
}
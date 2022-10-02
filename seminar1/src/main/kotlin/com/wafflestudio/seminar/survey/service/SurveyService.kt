package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.SeminarExceptionType
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface SurveyService {
    fun allSurveyList(): List<SurveyResponse>
    fun surveyForId(id: Long): SurveyResponse
    fun createSurvey(id: Long?, request: CreateSurveyRequest): SurveyResponse
}

@Service
class DefaultSurveyService(
    val surveyRepository: SurveyResponseRepository,
    val userRepository: UserRepository,
    val osRepository: OsRepository
): SurveyService {
    @Transactional
    override fun allSurveyList(): List<SurveyResponse> {
        val entityList = surveyRepository.findAll()
        return entityList.map(::SurveyResponse)
    }
    @Transactional
    override fun surveyForId(id: Long): SurveyResponse {
        val entity = surveyRepository.findById(id).orElseThrow() { throw Seminar404(SeminarExceptionType.NotExistSurveyForId) }
        return SurveyResponse(entity)
    }
    @Transactional
    override fun createSurvey(id: Long?, request: CreateSurveyRequest): SurveyResponse {
        val existId = id ?: throw java.lang.RuntimeException()
        val userEntity = userRepository.findById(existId).orElseThrow { java.lang.RuntimeException() }
        if (surveyRepository.existsByUser(userEntity)) { throw java.lang.RuntimeException() }
        val osEntity = osRepository.findByOsName(request.osName) ?: throw Seminar404(SeminarExceptionType.NotExistOSForName)
        return surveyRepository.save(request.toEntity(osEntity, userEntity)).toSurveyResponse()
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        SurveyResponse(
            id = id,
            user = user?.toUser(),
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
        )
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }
}
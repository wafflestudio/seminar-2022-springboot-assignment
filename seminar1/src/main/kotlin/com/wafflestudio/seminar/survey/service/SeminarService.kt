package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.api.response.UserDetailResponse
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.exception.UserNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun survey(userId: Long, request: CreateSurveyRequest): SurveyResponse
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
    private val userRepository: UserRepository,
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

    override fun survey(userId: Long, request: CreateSurveyRequest): SurveyResponse {
        val user = userRepository.findById(userId).orElseThrow { UserNotFoundException() }
        val os = osRepository.findByOsName(request.os) ?: throw Seminar404("${request.os} 이름의 OS가 없습니다.")
        val findSurvey = surveyResponseRepository.findByUserEntityId(userId)
        val survey = request.toEntity(user, os)
        if (findSurvey != null) {
            survey.id = findSurvey.id
        }
        val savedSurvey = surveyResponseRepository.save(survey)
        return SurveyResponse(savedSurvey)
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        SurveyResponse(
            id = id,
            user = if (userEntity != null) UserDetailResponse.from(userEntity!!) else null,
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
}
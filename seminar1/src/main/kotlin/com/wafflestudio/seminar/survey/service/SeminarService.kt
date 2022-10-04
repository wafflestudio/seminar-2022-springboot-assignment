package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.api.UserNotAuthorizedException
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun createSurvey(userId: Long?, survey: CreateSurveyRequest): SurveyResponse
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val userRepository: UserRepository,
    private val osRepository: OsRepository,
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

    override fun createSurvey(userId: Long?, survey: CreateSurveyRequest): SurveyResponse {
        if (userId == null) throw UserNotAuthorizedException()
        
        if (survey.operatingSystem == null) throw Seminar400("operating system is required")
        if (survey.springExp == -1) throw Seminar400("spring experience is required")
        if (survey.rdbExp == -1) throw Seminar400("rdb experience is required")
        if (survey.programmingExp == -1) throw Seminar400("programming experience is required")

        val userEntity = userRepository.findById(userId)
        if (userEntity.isEmpty) throw UserNotAuthorizedException()
        val osEntity = osRepository.findByOsName(survey.operatingSystem) ?: throw Seminar404("OS를 찾을 수 없어요.")

        val entity = SurveyResponseEntity(
            user = userEntity.get(),
            operatingSystem = osEntity,
            springExp = survey.springExp,
            rdbExp = survey.rdbExp,
            programmingExp = survey.programmingExp,
            major = survey.major,
            grade = survey.grade,
            timestamp = LocalDateTime.now(),
            backendReason = survey.backendReason,
            waffleReason = survey.waffleReason,
            somethingToSay = survey.somethingToSay,
        )
        val savedEntity = surveyResponseRepository.save(entity)
        return SurveyResponse(savedEntity)
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }
    
    private fun User(entity: UserEntity) = entity.run {
        User(id, nickname, email, password)
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        SurveyResponse(
            id = id,
            user = user?.let { User(it) },
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
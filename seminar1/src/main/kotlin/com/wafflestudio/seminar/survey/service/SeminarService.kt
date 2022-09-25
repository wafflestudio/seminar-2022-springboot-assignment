package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.api.User404
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun createSurveyResponse(userId: Long, request: CreateSurveyRequest): String
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
        )
    }

    override fun createSurveyResponse(userId: Long, request: CreateSurveyRequest): String {
        val userEntity: UserEntity = validateExistedUserId(userId)
        val osEntity: OperatingSystemEntity = validateExistedOsName(request.osName)
        
        val surveyResponse: SurveyResponseEntity = SurveyResponseEntity(
            operatingSystem = osEntity,
            springExp = request.springExp!!,
            rdbExp = request.rdbExp!!,
            programmingExp = request.programmingExp!!,
            major = request.major,
            grade = request.grade,
            timestamp = LocalDateTime.now().withNano(0),
            backendReason = request.backendReason,
            waffleReason = request.waffleReason,
            somethingToSay = request.somethingToSay,
            userEntity = userEntity
        )
        
        surveyResponseRepository.save(surveyResponse)
        return "create survey response succeed"
    }
    
    fun validateExistedUserId(userId: Long): UserEntity {
        return userRepository.findByIdOrNull(userId) ?: throw User404("해당 user id를 찾을 수 없습니다 - status 404")
    }

    fun validateExistedOsName(osName: String): OperatingSystemEntity {
        return osRepository.findByOsName(osName) ?: throw Seminar404("OS ${osName}을 찾을 수 없어요.")
    } 
    
}
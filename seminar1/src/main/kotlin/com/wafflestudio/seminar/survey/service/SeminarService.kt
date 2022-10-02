package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.exception.ErrorCode
import com.wafflestudio.seminar.exception.SeminarException
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun postSurvey(userId: Long, req: CreateSurveyRequest): String
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
    private val userRepository: UserRepository
) : SeminarService {
    
    override fun os(id: Long): OperatingSystem {
        val entity = osRepository.findByIdOrNull(id)
            ?: throw SeminarException(ErrorCode.OS_NOT_FOUND)
        return OperatingSystem(entity)
    }

    override fun os(name: String): OperatingSystem {
        val entity = osRepository.findByOsName(name)
            ?: throw SeminarException(ErrorCode.OS_NOT_FOUND)
        return OperatingSystem(entity)
    }

    override fun surveyResponseList(): List<SurveyResponse> {
        val surveyEntityList = surveyResponseRepository.findAll()
        return surveyEntityList.map(::SurveyResponse)
    }

    override fun surveyResponse(id: Long): SurveyResponse {
        val surveyEntity = surveyResponseRepository.findByIdOrNull(id)
            ?: throw SeminarException(ErrorCode.SURVEY_NOT_FOUND)
        return SurveyResponse(surveyEntity)
    }


    override fun postSurvey(userId: Long, req: CreateSurveyRequest): String {
        // 유저 정보 찾기
        val userEntity = userRepository.findByIdOrNull(userId)
            ?: throw SeminarException(ErrorCode.USER_NOT_FOUND)
        
        // OS 정보 찾기
        val osEntity = osRepository.findByOsName(req.os!!)
            ?: throw SeminarException(ErrorCode.OS_NOT_FOUND)
        
        surveyResponseRepository.save(
            SurveyResponseEntity(
                user = userEntity,
                operatingSystem = osEntity,
                springExp = req.springExp!!,
                rdbExp = req.rdbExp!!,
                programmingExp = req.programmingExp!!,
                major = req.major,
                grade = req.grade,
                timestamp = LocalDateTime.parse(
                    LocalDateTime.now().toString()
                    , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                backendReason = req.backendReason,
                waffleReason = req.waffleReason,
                somethingToSay = req.somethingToSay
            )
        )
        
        return "설문에 참여해주셔서 감사합니다."
    }
    
    
    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
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
            somethingToSay = somethingToSay
        )
    }
}
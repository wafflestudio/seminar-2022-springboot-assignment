package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.UserInfo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun postSurvey(id:Long, request: CreateSurveyRequest)
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

    @Transactional
    override fun postSurvey(id: Long, request: CreateSurveyRequest) {
        if (request.springExp == null || request.rdbExp == null || request.programmingExp == null) throw Seminar400("necessary info missing")
        
        val user = userRepository.findById(id)
            .orElseThrow { throw Seminar404("no user with given id") }
        if (surveyResponseRepository.findByUser(user) != null) throw Seminar409("survey done before")
        
        val surveyResponse = SurveyResponseEntity(
            user = user,
            operatingSystem = osRepository.findByOsName(request.os)
                ?: throw Seminar404("no os with given name"),
            springExp = request.springExp,
            rdbExp = request.rdbExp,
            programmingExp = request.programmingExp,
            major = request.major,
            grade = request.grade,
            timestamp = LocalDateTime.now(),
            backendReason = request.backendReason,
            waffleReason = request.waffleReason,
            somethingToSay = request.somethingToSay,
        )
        
        surveyResponseRepository.save(surveyResponse)
        
        user.surveyResponse = surveyResponse
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        SurveyResponse(
            id = id,
            user = user?.info() ?: UserInfo("", ""),
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
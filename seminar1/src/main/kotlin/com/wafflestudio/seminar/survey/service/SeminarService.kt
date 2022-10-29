package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.api.response.surveyInfo
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.*
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.exception.UserException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun doSurvey(Id: Long, createSurveyRequest: CreateSurveyRequest): surveyInfo
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

    override fun doSurvey(Id: Long, createSurveyRequest: CreateSurveyRequest): surveyInfo{
        if(
            createSurveyRequest.os == null || 
            createSurveyRequest.springExp == null || 
            createSurveyRequest.rdbExp == null || 
            createSurveyRequest.programmingExp == null
        )
            throw InCorrectInputException()
        
        val os = osRepository.findByOsName(createSurveyRequest.os) 
            ?: throw InCorrectOsNameException()
            
        val user = userRepository.findById(Id)
            .orElseThrow { 
                UserException(com.wafflestudio.seminar.user.exception.ErrorCode.NOT_EXISTS_USER)
            }
            
        
        if(surveyResponseRepository.findByUserEntityId(Id) != null){
            throw AlreadyExistsSurveyException()
        }
        
        val survey = createSurveyRequest.toSurvey(user, os)
        surveyResponseRepository.save(survey)
        return surveyInfo.toDTO(survey)
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
}
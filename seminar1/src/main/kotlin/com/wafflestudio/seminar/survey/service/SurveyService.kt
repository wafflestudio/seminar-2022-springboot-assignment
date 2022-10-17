package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar403
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.SeminarExceptionType
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
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
        return entityList.map { it.toSurveyResponse() }
    }
    @Transactional
    override fun surveyForId(id: Long): SurveyResponse {
        val entity = surveyRepository.findById(id).orElseThrow() { throw Seminar404(SeminarExceptionType.NotExistSurveyForId) }
        return entity.toSurveyResponse()
    }
    @Transactional
    override fun createSurvey(id: Long?, request: CreateSurveyRequest): SurveyResponse {
        val existId = id ?: throw Seminar403(SeminarExceptionType.NeedsAuthetication)
        val userEntity = userRepository.findById(existId).orElseThrow { throw Seminar404(SeminarExceptionType.NotExistUserId) }
        if (surveyRepository.existsByUser(userEntity)) { throw Seminar400(SeminarExceptionType.ExistUserSurvey) }
        val osEntity = osRepository.findByOsName(request.osName) ?: throw Seminar404(SeminarExceptionType.NotExistOSForName)
        return surveyRepository.save(request.toEntity(osEntity, userEntity)).toSurveyResponse()
    }
}
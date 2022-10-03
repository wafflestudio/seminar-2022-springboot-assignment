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
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun createSurveyResponse(createSurveyRequest: CreateSurveyRequest, xUserId: Long): SurveyResponse
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
            user = if (userId != null) {
                userRepository.findByIdOrNull(userId)!!.toUser()
            } else {
                null
            }
        )
    }

    override fun createSurveyResponse(createSurveyRequest: CreateSurveyRequest, xUserId: Long): SurveyResponse{
        val operatingSystem = osRepository.findByOsName(createSurveyRequest.operatingSystem) ?: throw Seminar404("서버에 존재하지 않는 운영체제입니다.")
        val userEntity = userRepository.findByIdOrNull(xUserId) ?: throw Seminar404("등록되지 않은 아이디입니다.")
        val springExpCond = (createSurveyRequest.springExp in 1..5)
        val rdbExpCond = (createSurveyRequest.rdbExp in 1..5)
        val programmingExpCond = (createSurveyRequest.programmingExp in 1..5)
        if(!springExpCond || !rdbExpCond || !programmingExpCond){
            throw Seminar400("유효하지 않은 응답입니다. 필수 문항에 대한 응답을 다시 확인해주세요.")
        }
        val surveyResponseEntity = SurveyResponseEntity(
            operatingSystem = operatingSystem,
            springExp = createSurveyRequest.springExp,
            rdbExp = createSurveyRequest.rdbExp,
            programmingExp = createSurveyRequest.programmingExp,
            major = createSurveyRequest.major,
            grade = createSurveyRequest.grade,
            timestamp = LocalDateTime.now(),
            backendReason = createSurveyRequest.backendReason,
            waffleReason = createSurveyRequest.waffleReason,
            somethingToSay = createSurveyRequest.somethingToSay,
            userId = xUserId
        )
        surveyResponseRepository.save(surveyResponseEntity)
        return SurveyResponse(surveyResponseEntity)
    }
}
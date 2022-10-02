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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun createSurvey(createSurveyDto : CreateSurveyRequest) :Long
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
            user_id = user?.toUser()
        )
    }
    override fun createSurvey(request: CreateSurveyRequest) :Long{
        val entity = request.run{SurveyResponseEntity(
            operatingSystem= osRepository.findByOsName(osName?: throw Seminar400("os가 입력되지 않았습니다.")) ?: throw Seminar404("존재하지 않는 os를 입력했습니다."),
            springExp= springExp?: throw Seminar400("Spring 경험이 입력되지 않았습니다."),
            rdbExp= rdbExp?: throw Seminar400("rdb 경험이 입력되지 않았습니다."),
            programmingExp= programmingExp?: throw Seminar400("프로그래밍 경험이 입력되지 않았습니다.") ,
            timestamp = timestamp,
            major= major,
            grade= grade,
            backendReason = backendReason,
            waffleReason = waffleReason,
            somethingToSay = somethingToSay,
            user = userRepository.findById(user_id).orElse(null)?: throw Seminar400("존재하지 않는 User ID로 설문을 시도했습니다.")
        )
        }
        
        surveyResponseRepository.save(entity)
        
        return entity.id 
    }
}
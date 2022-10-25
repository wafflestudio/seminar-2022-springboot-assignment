package com.wafflestudio.seminar.survey.service

<<<<<<< HEAD

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
=======
import com.wafflestudio.seminar.survey.api.Seminar404
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
<<<<<<< HEAD
import com.wafflestudio.seminar.exception.Seminar400
import com.wafflestudio.seminar.exception.Seminar404
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
=======
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
<<<<<<< HEAD
    fun postSurvey(id: Long, req : CreateSurveyRequest): String
=======
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
<<<<<<< HEAD
    private val userRepository: UserRepository
=======
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
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
<<<<<<< HEAD
            user = userEntity?.toUser()
        )
    }
    
    @Transactional
    override fun postSurvey(id: Long, req : CreateSurveyRequest): String {
        if (req.springExp == null || req.rdbExp == null || req.programmingExp == null || req.osName.isNullOrEmpty()) {
            throw Seminar400("설문을 완료하지 않았습니다.")
        } else {
            val user = userRepository.findByIdOrNull(id) ?: throw Seminar404("존재하지 않는 유저입니다.")
            val operatingSystem = osRepository.findByOsName(req.osName) ?: throw Seminar404("존재하지 않는 OS입니다.")
            surveyResponseRepository.save(
                SurveyResponseEntity(
                    operatingSystem = operatingSystem,
                    springExp = req.springExp,
                    rdbExp = req.rdbExp,
                    programmingExp = req.programmingExp,
                    major = req.major,
                    grade = req.grade,
                    timestamp = LocalDateTime.now().withNano(0),
                    backendReason = req.backendReason,
                    waffleReason = req.waffleReason,
                    somethingToSay = req.somethingToSay,
                    userEntity = user
                )
            )
            return "설문조사가 완료되었습니다."
        }
        
    }
=======
        )
    }
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
}
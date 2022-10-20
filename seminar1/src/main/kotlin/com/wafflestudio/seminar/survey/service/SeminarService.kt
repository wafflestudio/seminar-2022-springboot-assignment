package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar403
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
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
    fun makeSurvey(req: CreateSurveyRequest, userId: Long?): String
}

@Suppress("UnusedEquals")
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

    override fun makeSurvey(req: CreateSurveyRequest, userId: Long?): String {
        if(userId==null) throw Seminar403("유저 번호를 입력해주세요.")
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw Seminar403("존재하지 않는 유저입니다.")
        if(req.springExp==null || req.rdbExp==null || req.programmingExp==null) throw Seminar400("값이 부족합니다.")
        val osEntity = osRepository.findByOsName(req.os!!) ?: throw Seminar404("존재하지 않는 OS입니다.")
        val surveyResponseEntity = req.run { 
            SurveyResponseEntity(
                osEntity,
                springExp!!,
                rdbExp!!,
                programmingExp!!,
                major!!,
                grade!!,
                LocalDateTime.now(),
                backendReason,
                waffleReason,
                somethingToSay,
                user = userEntity
            )
        }
        surveyResponseRepository.save(surveyResponseEntity)
        return "설문조사 등록이 완료되었습니다."
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun SurveyResponse(entity: SurveyResponseEntity): SurveyResponse {
        return if (entity.user == null) entity.run {
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
                user = null
            )
        } else return entity.run {
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
                user = User(user!!.id, user!!.name, user!!.email, user!!.encPwd)
            )
        }
    }
}
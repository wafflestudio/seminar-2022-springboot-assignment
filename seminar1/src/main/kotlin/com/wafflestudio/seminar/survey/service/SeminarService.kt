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
import com.wafflestudio.seminar.user.api.User403
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.UserInfo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
    fun addSurveyResponse(userId: Long?, createSurveyRequest: CreateSurveyRequest)
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

    override fun addSurveyResponse(userId: Long?, createSurveyRequest: CreateSurveyRequest) {
        if (createSurveyRequest.springExp == 0 || createSurveyRequest.rdbExp == 0 || createSurveyRequest.programmingExp == 0) throw Seminar400(
            "BAD REQUEST"
        )
        if (userId == null) throw User403("Not Signed In")
        userRepository.findByIdOrNull(userId) ?: throw Seminar404("User Id Does Not Exist")
        val os =
            osRepository.findByOsName(createSurveyRequest.operatingSystem) ?: throw Seminar404("Os Name Does not Exist")
        surveyResponseRepository.save(
            SurveyResponseEntity(
                operatingSystem = os,
                userId = userId,
                springExp = createSurveyRequest.springExp,
                rdbExp = createSurveyRequest.rdbExp,
                programmingExp = createSurveyRequest.programmingExp,
                major = createSurveyRequest.major,
                grade = createSurveyRequest.grade,
                backendReason = createSurveyRequest.backendReason,
                waffleReason = createSurveyRequest.waffleReason,
                somethingToSay = createSurveyRequest.somethingToSay
            )
        )
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }

    private fun UserInfo(entity: UserEntity) = entity.run {
        UserInfo(nickname, email)
    }

    private fun SurveyResponse(entity: SurveyResponseEntity) = entity.run {
        var userInfo: UserInfo? = null
        if (entity.userId != null) {
            val userEntity = userRepository.findByIdOrNull(entity.userId) ?: throw Seminar404(
                "User Id Does Not Exist"
            )
            userInfo = UserInfo(userEntity)
        }
        SurveyResponse(
            id = id,
            user = userInfo,
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
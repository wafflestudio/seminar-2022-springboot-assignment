package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun os(name: String): OperatingSystem
    fun os(id: Long): OperatingSystem
    fun surveyResponseList(): List<SurveyResponse>
    fun surveyResponse(id: Long): SurveyResponse
}

@Service
class SeminarServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository,
) : SeminarService {
    override fun os(id: Long): OperatingSystem {
        return osRepository.findByIdOrNull(id) ?: throw Seminar404("OS를 찾을 수 없어요.")
    }

    override fun os(name: String): OperatingSystem {
        return osRepository.findByOsName(name) ?: throw Seminar404("OS ${name}을 찾을 수 없어요.")
    }

    override fun surveyResponseList(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun surveyResponse(id: Long): SurveyResponse {
        return surveyResponseRepository.findByIdOrNull(id) ?: throw Seminar404("설문 결과를 찾을 수 없어요.")
    }
}
package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponse
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SurveyServiceImpl(
    private val surveyResponseRepository: SurveyResponseRepository
) : SurveyService {
    override fun findAll(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun findById(id: Long): SurveyResponse {
        return surveyResponseRepository.findById(id) ?: throw SeminarException("해당하는 id의 설문 결과가 없습니다!", HttpStatus.NOT_FOUND)
    }
}
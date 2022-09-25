package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarException
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

interface SurveyResponseRepository {
    fun findAll(): List<SurveyResponse>
    fun findById(id: Long): SurveyResponse
}

@Component
class SurveyResponseRepositoryImpl(
    db: MemoryDB
) : SurveyResponseRepository {
    private val surveyList = db.getSurveyResponses()

    override fun findAll(): List<SurveyResponse> {
        return surveyList
    }

    override fun findById(id: Long): SurveyResponse {
        return surveyList.find { it.id == id } ?: throw SeminarException("ID에 해당하는 설문 결과가 없어요.")
    }
}
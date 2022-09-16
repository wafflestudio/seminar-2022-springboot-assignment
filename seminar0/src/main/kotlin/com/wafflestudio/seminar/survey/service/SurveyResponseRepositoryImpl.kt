package com.wafflestudio.seminar.survey.service


import com.wafflestudio.seminar.survey.database.SurveyResponseDB
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.domain.SurveyResponseForClient
import com.wafflestudio.seminar.survey.exception.IDNotFoundException
import org.springframework.stereotype.Service

@Service
class SurveyResponseRepositoryImpl(
    private val dataBase: SurveyResponseDB
) : SurveyResponseRepository {

    override fun findAll(): List<SurveyResponseForClient> {
        val result = mutableListOf<SurveyResponseForClient>()
        for (response in dataBase.getSurveyResponses()) {
            result.add(surveyResponseReviseForClient(response))
        }
        return result
    }

    override fun findById(id: Long): SurveyResponseForClient {
        try {
            return surveyResponseReviseForClient(dataBase.getSurveyResponseById(id))
        } catch (e: IDNotFoundException) {
            throw e
        }
    }

    fun surveyResponseReviseForClient(surveyResponse: SurveyResponse): SurveyResponseForClient {
        return SurveyResponseForClient(
            surveyResponse.component1(),
            surveyResponse.component2().osName,
            surveyResponse.component3(),
            surveyResponse.component4(),
            surveyResponse.component5(),
            surveyResponse.component6(),
            surveyResponse.component7()
        )
    }
}
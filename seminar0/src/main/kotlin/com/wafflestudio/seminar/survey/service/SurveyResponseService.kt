package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.survey.SurveyResponseRepository
import org.springframework.stereotype.Service

@Service
class SurveyResponseService(
    val surveyResponseRepository: SurveyResponseRepository
) {
}
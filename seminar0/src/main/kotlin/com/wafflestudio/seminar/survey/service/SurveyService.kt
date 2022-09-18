package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import org.springframework.stereotype.Service

@Service
class SurveyService(private val surveyRepository : SurveyResponseRepository) {
    fun getAllSurveys() = surveyRepository.findAll()
    fun searchSurveyById(id: Long) = surveyRepository.findById(id)
}
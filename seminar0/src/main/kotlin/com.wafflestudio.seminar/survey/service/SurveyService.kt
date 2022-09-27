package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
interface SurveyService {
    fun findAll() : List<SurveyResponse>
    fun findById(id : Long) : SurveyResponse?
}
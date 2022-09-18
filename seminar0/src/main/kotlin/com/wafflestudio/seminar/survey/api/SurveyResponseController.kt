package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SurveyResponseController(
    private val surveyResponseService: SurveyResponseService,
) {
    @GetMapping("/survey-responses")
    fun getAllSurveyResponses(): List<SurveyResponse>{
        return surveyResponseService.getAllSurveyResponses()
    }
    
    @GetMapping("/survey-responses/{id}")
    fun getSurveyResponseById(
        @PathVariable id: Long,
    ): SurveyResponse{
        return surveyResponseService.getSurveyResponseById(id)
    }
}
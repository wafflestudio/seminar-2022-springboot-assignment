package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SurveyResponseController(
    private val surveyResponseService: SurveyResponseService
) {
    
    @GetMapping("/survey-response")
    fun getAll(): List<SurveyResponse> {
        return surveyResponseService.findAll()
    }
    
    @GetMapping("/survey-response/{id}")    
    fun getByID(@PathVariable id: Long): SurveyResponse {
        return surveyResponseService.findById(id)
    }
    
}
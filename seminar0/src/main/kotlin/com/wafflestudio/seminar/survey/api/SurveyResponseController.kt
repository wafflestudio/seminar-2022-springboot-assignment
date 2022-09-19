package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SurveyResponseController(val surveyResponseService: SurveyResponseService) {
    @GetMapping("survey")
    fun findAll(): List<SurveyResponse>{
        return  surveyResponseService.findAll()
    }
    
    @GetMapping("survey/{id}")
    fun findById(@PathVariable id: Long): SurveyResponse {
        return  surveyResponseService.findById(id)
    }
}
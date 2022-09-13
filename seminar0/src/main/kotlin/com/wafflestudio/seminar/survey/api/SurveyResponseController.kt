package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SurveyResponseController(val surveyResponseService: SurveyResponseService) {
    
    @GetMapping("/surveys")
    fun getSurveys():ResponseEntity<List<SurveyResponse>> {
        return ResponseEntity.ok().body(surveyResponseService.getAllSurveyResponses())
    }
    
    @GetMapping("/survey/{surveyId}")
    fun getSurvey(@PathVariable("surveyId") surveyId: Long) : ResponseEntity<SurveyResponse> {
        return ResponseEntity.ok().body(surveyResponseService.getSurveyResponseById(surveyId))
    }
}
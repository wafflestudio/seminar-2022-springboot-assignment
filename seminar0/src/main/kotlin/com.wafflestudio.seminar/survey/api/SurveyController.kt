package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.exception.SeminarException
import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SurveyController(private val surveyService: SurveyService) {
    
    @GetMapping("/total")
    fun getTotalSurvey() = surveyService.searchAllSurveys()
    
    @GetMapping("/{id}")
    fun getSurvey(@PathVariable id: Long) =surveyService.searchById(id) 
    
    
}
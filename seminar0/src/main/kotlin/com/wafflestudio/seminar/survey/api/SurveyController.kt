package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SurveyController(private val surveyService: SurveyService) {
    @GetMapping("/total")
    fun getTotalSurvey() = surveyService.getAllSurveys()

    @GetMapping("/{id}")
    fun getSurvey(@PathVariable id: Long) =surveyService.searchSurveyById(id)
}
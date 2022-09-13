package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SurveyResponseController(
    private val surveyResponseService: SurveyResponseService
) {

    @GetMapping("")
    fun getAll(): List<SurveyResponse> {
        return surveyResponseService.findSurveys()
    }
    
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: String): SurveyResponse {
        return surveyResponseService.findById(id.toLong())
    }
}
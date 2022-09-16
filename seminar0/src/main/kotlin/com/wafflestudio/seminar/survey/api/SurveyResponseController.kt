package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class SurveyResponseController(private val surveyResponseService: SurveyResponseService) {
    @GetMapping
    fun getAll(): List<SurveyResponse> {
        return surveyResponseService.findAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): SurveyResponse {
        val res = surveyResponseService.findBySurveyId(id)
        if (res.isEmpty()){
            throw SeminarExceptionHandler().SeminarException("Page not found", HttpStatus.NOT_FOUND)
        }
        return res[0] 
    }
}
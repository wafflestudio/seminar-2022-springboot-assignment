package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import com.wafflestudio.seminar.survey.service.SurveyResponseServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/response")
class SurveyController (private val surveyResponseService: SurveyResponseService) {
    @GetMapping
    fun getAllSurveyResponse(): List<SurveyResponse>{
        return surveyResponseService.findAll()
    }
    
    @GetMapping("/{id}")
    fun getSurveyResponseById(@PathVariable id: Long): SurveyResponse{
        val result = surveyResponseService.findById(id)
        if (result == null){
            throw SeminarExceptionHandler().SeminarException("Error: Invalid Id", HttpStatus.NOT_FOUND)
        }
        return result
    }
}
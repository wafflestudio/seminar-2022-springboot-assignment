package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequestMapping("/survey")
class SurveyResponseController(
    private val surveyResponseService: SurveyResponseService
) {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(e.message ?: "Illegal Argument", HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(e.message ?: "Exception", HttpStatus.INTERNAL_SERVER_ERROR)
    }
    

    @GetMapping("")
    fun getAll(): List<SurveyResponse> {
        return surveyResponseService.findSurveys()
    }
    
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: String): SurveyResponse {
        return surveyResponseService.findById(id.toLong())
    }
}
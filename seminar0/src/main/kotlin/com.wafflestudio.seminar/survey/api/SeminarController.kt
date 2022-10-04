package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.OsService
import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SeminarController (
    private val surveyService : SurveyService,
    private val osService: OsService
) {
    @GetMapping("/list")
    fun findAll() : List<SurveyResponse>{
        return surveyService.findAll()
    }
    
    @GetMapping("/id/{idx}")
    fun findById(
        @PathVariable idx : Long
    ) : SurveyResponse?{
        return surveyService.findById(idx) ?: throw SeminarExceptionHandler.SeminarException()
    }
    
    @GetMapping("/os/{idx}")
    fun osFindById(
        @PathVariable idx : Long
    ) : OperatingSystem?{
        return osService.findById(idx) ?: throw SeminarExceptionHandler.OsException()
    }
    
    @GetMapping("/name")
    fun osFindByName(
        @RequestParam name: String
    ) : OperatingSystem?{
        return osService.findByName(name) ?: throw SeminarExceptionHandler.NameException()
    }
}
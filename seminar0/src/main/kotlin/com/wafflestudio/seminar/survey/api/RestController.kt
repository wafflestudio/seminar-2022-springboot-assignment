package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class RestController(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository
) {
    
    @GetMapping("/srAll")
    fun findSurveyResponseAll(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }
    
    @GetMapping("/srById")
    fun findSurveyResponseById(
        @RequestParam id: Long
    ): SurveyResponse {
        return surveyResponseRepository.findById(id)
    }
    
    @GetMapping("/osById")
    fun findOSById(
        @RequestParam id: Long
    ): OperatingSystem {
        return osRepository.findById(id)
    }
    
    @GetMapping("/osByName")
    fun findOSByOSName(
        @RequestParam osName: String
    ): List<OperatingSystem> {
        return osRepository.findByOSName(osName)
    }
}
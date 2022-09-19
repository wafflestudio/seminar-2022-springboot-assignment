package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.SurveyResponse
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SurveyController(
    val osService: OsService,
    val surveyService: SurveyService
) {
    @GetMapping("/survey")
    fun getSurveyResponse(): List<SurveyResponse> {
        return surveyService.findAll()
    }

    @GetMapping("/survey/{userId}")
    fun getSurveyResponse(
        @PathVariable userId: Long
    ): SurveyResponse {
        return surveyService.findById(userId)
    }

    @GetMapping("/os/{osId}")
    fun getOs(
        @PathVariable osId: Long
    ): OperatingSystem {
        return osService.findById(osId)
    }

    @GetMapping("/os")
    fun getOs(
        @RequestParam osName: String
    ): OperatingSystem {
        return osService.findByName(osName)
    }
}
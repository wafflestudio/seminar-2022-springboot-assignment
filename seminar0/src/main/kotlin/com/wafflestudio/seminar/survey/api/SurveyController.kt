package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SurveyController(
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
) {
    @GetMapping("/os")
    fun getOs(): List<OperatingSystem> {
        return osRepository.findAll()
    }

    @GetMapping("/os/{userId}")
    fun getOs(
        @PathVariable userId: Long
    ): OperatingSystem {
        return osRepository.findById(userId)
    }

    @GetMapping("/survey")
    fun getSurveyResponse(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    @GetMapping("/survey/{userId}")
    fun getSurveyResponse(
        @PathVariable userId: Long
    ): SurveyResponse {
        return surveyResponseRepository.findById(userId)
    }
}
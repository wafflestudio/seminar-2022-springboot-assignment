package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SeminarService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class SeminarController(
    private val service: SeminarService
) {
    @GetMapping("/os/{osId}")
    fun getOs(
        @PathVariable osId: Long,
    ) = service.os(osId)

    @GetMapping("/os")
    fun getOs(
        @RequestParam name: String
    ) = service.os(name)

    @GetMapping("/survey")
    fun getSurveyList() =
        service.surveyResponseList()

    @GetMapping("/survey/{surveyId}")
    fun getSurvey(
        @PathVariable surveyId: Long,
    ) = service.surveyResponse(surveyId)
    
    @PostMapping("/api/v1/survey")
    fun createSurvey(
        @Valid @RequestBody createSurveyRequest: CreateSurveyRequest,
        @RequestHeader("X-User-ID") xUserId : Long,
    ): SurveyResponse {
        return service.createSurveyResponse(createSurveyRequest, xUserId)
    }
}
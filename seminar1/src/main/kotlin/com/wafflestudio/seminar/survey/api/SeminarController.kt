package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import org.springframework.web.bind.annotation.*

@RestController
class SeminarController(
    private val seminarService: SeminarService,
) {
    @GetMapping("/os/{osId}")
    fun getOs(
        @PathVariable osId: Long,
    ) = seminarService.os(osId)

    @GetMapping("/os")
    fun getOs(
        @RequestParam name: String
    ) = seminarService.os(name)

    @GetMapping("/survey")
    fun getSurveyList() =
        seminarService.surveyResponseList()

    @GetMapping("/survey/{surveyId}")
    fun getSurvey(
        @PathVariable surveyId: Long,
    ) = seminarService.surveyResponse(surveyId)

    @PostMapping("/api/v1/survey")
    fun postSurvey(
        @RequestHeader("X-User-Id") id: Long?,
        @RequestBody request: CreateSurveyRequest
    ): String {
        seminarService.postSurvey(id ?: throw Seminar403("cannot identify user"), request)
        return "survey created"
    }

}
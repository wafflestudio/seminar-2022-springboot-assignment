package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.exception.UserUnauthorizedException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

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
    fun survey(@RequestHeader("X-User-ID") userId: Long?,
               @RequestBody @Valid createSurveyRequest: CreateSurveyRequest) : SurveyResponse
    {
        userId ?: throw UserUnauthorizedException()
        return seminarService.survey(userId, createSurveyRequest);
    }
}
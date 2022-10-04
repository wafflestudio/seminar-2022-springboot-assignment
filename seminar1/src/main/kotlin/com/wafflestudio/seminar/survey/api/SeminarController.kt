package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.api.GetUserUnauthorizedException
import org.springframework.validation.BindingResult
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
            @Valid @RequestBody surveyRequest: CreateSurveyRequest,
            bindingResult: BindingResult,
            @RequestHeader(name="X-User-ID") id: Long?,
    ) {
        if (bindingResult.hasErrors()) {
            throw SurveyRequestBodyException(bindingResult.fieldErrors)
        }
        if (id == null) { throw GetUserUnauthorizedException() }
        service.createSurvey(surveyRequest, id)
    } 

}
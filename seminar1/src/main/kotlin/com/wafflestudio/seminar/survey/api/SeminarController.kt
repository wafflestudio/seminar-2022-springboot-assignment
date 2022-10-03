package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
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

    @PostMapping("/api/v1/user")
    fun createUser(
        @RequestBody @Valid createUserRequest: CreateUserRequest,
    ) = service.createUser(createUserRequest)

    @PostMapping("/api/v1/login")
    fun loginUser(
        @RequestBody @Valid loginUserRequest: LoginUserRequest,
    ) = service.loginUser(loginUserRequest)

    @GetMapping("/api/v1/user/me")
    fun getUser(
        @RequestHeader("X-User-ID") userId: Long,
    ) = service.user(userId)

    @PostMapping("/api/v1/survey")
    fun createSurvey(
        @RequestBody @Valid createSurveyRequest: CreateSurveyRequest,
        @RequestHeader("X-User-ID") userId: Long,
    ) = service.createSurvey(createSurveyRequest, userId)
}
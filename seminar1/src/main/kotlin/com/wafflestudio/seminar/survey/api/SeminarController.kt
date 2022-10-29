package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.api.response.surveyInfo
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.exception.ErrorCode
import com.wafflestudio.seminar.user.exception.UserException
import com.wafflestudio.seminar.user.exception.UserUnauthorized
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
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
    
    @PostMapping("/survey")
    fun postSurvey(
        @RequestHeader("X-User-ID") userId: Long?,
        @RequestBody createSurveyRequest: CreateSurveyRequest
    ): surveyInfo {
        userId ?: throw UserUnauthorized()
        return service.doSurvey(userId, createSurveyRequest)
    }
}
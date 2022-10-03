package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

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
    fun takeSurvey (
//            @RequestBody survey: CreateSurveyRequest,

            @RequestParam operatingSystem: String?,
            @RequestParam springExp: Int?,
            @RequestParam rdbExp: Int?,
            @RequestParam programmingExp: Int?,
            @RequestParam major: String?,
            @RequestParam grade: String?,
            @RequestParam timestamp: LocalDateTime?,
            @RequestParam backendReason: String?,
            @RequestParam waffleReason: String?,
            @RequestParam somethingToSay: String?,
            @RequestHeader("X-User-ID") id: Long?
    ): String{
        id ?: throw Seminar403("id를 입력해주세요.")
        
        operatingSystem ?: throw Seminar400("os를 입력해주세요.")
        springExp ?: throw Seminar400("springExp를 입력해주세요.")
        rdbExp ?: throw Seminar400("rdbExp를 입력해주세요.")
        programmingExp ?: throw Seminar400("programmingExp를 입력해주세요.")
        
        val survey = CreateSurveyRequest(operatingSystem, springExp, rdbExp, programmingExp, major, grade, timestamp, backendReason, waffleReason, somethingToSay)
        return service.takeSurvey(survey, id)
    }
    
}
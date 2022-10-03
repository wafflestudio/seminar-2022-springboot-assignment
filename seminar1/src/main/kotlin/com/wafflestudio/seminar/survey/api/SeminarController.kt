package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
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
            @RequestHeader(value = "X-User-Id", required = false) userId: Long?,
            @RequestBody req: CreateSurveyRequest
    ): String{
        userId ?: throw Seminar403("id를 확인하세요.")
        req.operatingSystem ?: throw Seminar400("os를 확인하세요.")
        req.programmingExp ?: throw Seminar400("programmingExp를 확인하세요.")
        req.springExp ?: throw Seminar400("springExp를 확인하세요.")
        req.rdbExp ?: throw Seminar400("rdbExp를 확인하세요.")
        
        return service.postSurvey(req, userId)
    }

}
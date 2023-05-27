package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.exception.ErrorCode
import com.wafflestudio.seminar.exception.SeminarException
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
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
        @RequestHeader(value="X-User-Id", required=false) userId : Long?,
        @RequestBody req : CreateSurveyRequest
    ) : String {
        
        if (userId == null) throw SeminarException(ErrorCode.FORBIDDEN)
        else if (
            req.os == null || req.springExp == null
                || req.rdbExp == null || req.programmingExp == null
        ) throw SeminarException(ErrorCode.INVALID_PARAMETER)
        else return service.postSurvey(userId, req)
    }
}
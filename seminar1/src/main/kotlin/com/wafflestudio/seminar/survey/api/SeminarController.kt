package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.api.MissingHeaderException
import com.wafflestudio.seminar.user.api.MissingValueException
import org.springframework.web.bind.annotation.*

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
    fun postSurvey(
            @RequestHeader(value = "X-User-ID", required = false) id: Long?,
            @RequestBody req: CreateSurveyRequest
    ) : String{
        if (id == null) throw MissingHeaderException("유저를 식별할 수 없습니다.")
        if (req.springExp == null || req.rdbExp == null || req.programmingExp == null || req.os == null)
            throw MissingValueException("필수적인 항목을 모두 작성해주세요.")
        return service.postSurvey(id, req)
    }
}
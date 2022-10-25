package com.wafflestudio.seminar.survey.api

<<<<<<< HEAD
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.exception.Seminar403
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
=======
import com.wafflestudio.seminar.survey.service.SeminarService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
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
<<<<<<< HEAD
    
    @PostMapping("/survey")
    fun postSurvey(
        @RequestHeader(value = "X-User-ID", required = false) id : Long?,
        @RequestBody req: CreateSurveyRequest
    ) : String {
        if (id == null) {
            throw Seminar403("정의한 헤더가 존재하지 않습니다.")
        } else {
            return service.postSurvey(id, req)
        }
    }
    
=======
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc

}
package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.exception.Seminar400
import com.wafflestudio.seminar.exception.Seminar403
import com.wafflestudio.seminar.exception.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class SeminarController(
    private val service: SeminarService,
    private val userRepository: UserRepository
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
    fun survey(
        @RequestBody req: CreateSurveyRequest,
        @RequestHeader("X-User-ID") userId : Long?
    )
    {
        if(userId == null)
            throw Seminar403("로그인해주세요")
        val userEntity = userRepository.findByUserId(userId)
        if(req.spring_exp == null || req.rdb_exp == null || req.programming_exp == null || req.os_name == null || req.major == null || req.grade == null)
        {
            throw Seminar400("질문에 빠짐없이 답해주세요")
        }
        val os = service.osEntity(req.os_name)
        

        val newSurveyResponse = SurveyResponseEntity(userEntity, os, req.spring_exp, req.rdb_exp, req.programming_exp, req.major, req.grade, LocalDateTime.now(), req.backendReason, req.waffleReason, req.somethingToSay)
        service.createSurveyResponse(newSurveyResponse)
    }

}
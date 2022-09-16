package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponseForClient
import com.wafflestudio.seminar.survey.service.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class RestController(
    private val service: Service,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val osRepository: OsRepository
) {

    @GetMapping("/survey_response/all")
    fun getSurveyResponseAll(): List<SurveyResponseForClient> {
        return surveyResponseRepository.findAll()
    }

    @GetMapping("/survey_response/id")
    fun getSurveyResponseById(
        @RequestHeader id: Long
    ): SurveyResponseForClient {
        return surveyResponseRepository.findById(id)
    }


    @GetMapping("/operating_system/id")
    fun getOperatingSystemById(
        @RequestHeader id: Long
    ): OperatingSystem {
        return osRepository.findById(id)
    }

    @GetMapping("/operating_system/name")
    fun getOperatingSystemByOSName(
        @RequestParam name: String
    ): OperatingSystem {
        return osRepository.findByOSName(name)
    }
}
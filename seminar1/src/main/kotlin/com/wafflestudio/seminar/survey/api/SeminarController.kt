package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.OSService
import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/survey")
public class SurveyController(
    val service: SurveyService
) {
    @GetMapping
    fun getAllSurveys(): List<SurveyResponse> {
        return service.allSurveyList()
    }

    @GetMapping("/{surveyId}")
    fun getSurveyById(
        @PathVariable surveyId: Long,
    ): SurveyResponse {
        return service.surveyForId(surveyId)
    }
    
    @PostMapping
    fun createSurvey(
        @RequestBody request: CreateSurveyRequest,
        @RequestHeader("X-User-Id") id: Long?
    ): SurveyResponse {
      return service.createSurvey(id, request)  
    } 
}

@RestController
@RequestMapping("api/v1/os")
public class OSController(
    val service: OSService
) {
    @GetMapping("/{id}")
    fun getOsById(
        @PathVariable id: Long,
    ): OperatingSystem {
        return service.osForId(id)
    }

    @GetMapping
    fun getOsByName(
        @RequestParam name: String
    ): OperatingSystem {
        return service.osForName(name)
    }
}
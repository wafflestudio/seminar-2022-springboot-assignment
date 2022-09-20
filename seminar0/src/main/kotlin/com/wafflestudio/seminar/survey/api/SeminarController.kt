package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.OSService
import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/survey")
public class SurveyController(
    val service: SurveyService
) {
    @GetMapping("")
    fun getAllSurveys(): List<SurveyResponse> {
        return service.allSurveyList()
    }

    @GetMapping("/{surveyId}")
    fun getSurveyById(
        @PathVariable surveyId: Long,
    ): SurveyResponse {
        return service.surveyForId(surveyId)
    }
}

@RestController
@RequestMapping("/os")
public class OSController(
    val service: OSService
) {
    @GetMapping("/{id}")
    fun getOsById(
        @PathVariable id: Long,
    ): OperatingSystem {
        return service.osForId(id)
    }

    @GetMapping("")
    fun getOsByName(
        @RequestParam name: String
    ): OperatingSystem {
       return service.osForName(name) 
    }
}
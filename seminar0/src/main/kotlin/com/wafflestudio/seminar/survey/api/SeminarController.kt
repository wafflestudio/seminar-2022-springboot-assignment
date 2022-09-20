package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.OSService
import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/survey")
public class SurveyController(
    service: SurveyService
) {
    @GetMapping("")
    fun getAllSurveys() {
        
    }

    @GetMapping("/{surveyId}")
    fun getSurveyById(
        @PathVariable surveyId: Long,
    ) {
        
    }
}

@RestController
@RequestMapping("/os")
public class OSController(
    service: OSService
) {
    @GetMapping("/{id}")
    fun getOsById(
        @PathVariable id: Long,
    ) {
        
    }

    @GetMapping("")
    fun getOsByName(
        @RequestParam name: String
    ) {
        
    }
}
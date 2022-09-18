package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.SurveyService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SurveyController(private val surveyService: SurveyService) {
}
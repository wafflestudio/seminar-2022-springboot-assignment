package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {
    @GetMapping("/survey-all")
    fun getAllSurvey(): List<SurveyResponse> {
        return userService.getAllSurvey()
    }
    
    @GetMapping("/survey-id")
    fun getSurveyId(@RequestHeader(name = "id", required = true) id: Long): SurveyResponse {
        return userService.getSurveyId(id)
    }
}
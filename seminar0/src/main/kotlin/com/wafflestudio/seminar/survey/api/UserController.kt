package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {
    @GetMapping("/survey-all")
    fun getAllSurvey(): List<SurveyResponse> {
        return userService.getAllSurvey()
    }
    
    @GetMapping("/survey-id")
    fun getSurveyOfId(@RequestHeader(name = "id", required = true) id: Long): SurveyResponse {
        return userService.getSurveyOfId(id)
    }
    
    @GetMapping("/os-id")
    fun getOSOfId(@RequestHeader(name = "id", required = true) id: Long): OperatingSystem {
        return userService.getOSOfId(id)
    }
    
    @GetMapping("/os-name")
    fun getOSOfName(@RequestParam(name = "name", required = true)name: String): OperatingSystem {
        return userService.getOSOfName(name)
    }
}
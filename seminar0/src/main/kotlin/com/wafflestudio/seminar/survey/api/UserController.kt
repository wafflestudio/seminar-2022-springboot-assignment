package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {
    @GetMapping("/survey-all")
    fun getAllSurvey(): List<SurveyResponse> {
        return userService.getAllSurvey()
    }

    @GetMapping("/survey-id/{id}")
    fun getSurveyOfId(@PathVariable id: Long): SurveyResponse {
        return userService.getSurveyOfId(id)
    }

    @GetMapping("/os-id/{id}")
    fun getOSOfId(@PathVariable id: Long): OperatingSystem {
        return userService.getOSOfId(id)
    }

    @GetMapping("/os-name")
    fun getOSOfName(@RequestParam(name = "name", required = true)name: String): OperatingSystem {
        return userService.getOSOfName(name)
    }
}
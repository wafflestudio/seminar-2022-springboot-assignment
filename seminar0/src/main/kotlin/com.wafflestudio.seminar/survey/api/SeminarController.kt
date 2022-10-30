package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SeminarController (
    private val userService: UserService
    ) {
        @GetMapping("/surveyAll")
        fun putAll(): List<SurveyResponse> {
            return userService.showAll()
        }

        @GetMapping("/surveyId")
        fun putResult(
                @RequestHeader("id") id: Long,
        ): SurveyResponse {
            return userService.showSurveyById(id)
        }

        @GetMapping("/OsId")
        fun putOS(
                @RequestHeader("id") id: Long,
        ): OperatingSystem {
            return userService.showOsById(id)
        }

        @GetMapping("/OsName")
        fun osName(
                @RequestParam os: String,
        ): OperatingSystem {
            return userService.showOsByOsName(os)
        }
    }
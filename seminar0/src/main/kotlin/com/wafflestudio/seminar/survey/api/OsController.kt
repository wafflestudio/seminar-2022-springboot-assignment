package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class OsController(
    private val osService: OsService
) {
    @GetMapping("os/")
    fun getAllResponses(): List<OperatingSystem> {
        return osService.getAllOs()
    }
    @GetMapping("os/{id}/")
    fun geyResponseById(@PathVariable("id") id:Long):OperatingSystem{
        
            return osService.getOsById(id)
    }
}
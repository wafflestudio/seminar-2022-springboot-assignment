package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.Service
import jdk.jshell.spi.ExecutionControlProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/os")
class OsController(private val service: Service) {

    @GetMapping("/{id}")
    fun getOsById(@PathVariable id: Long)= service.findByOsId(id)?:throw
    Exception("")

    /** need to be implemented**/
    @GetMapping()
    fun getOsById(@RequestParam("name") name: String)= service.findByOsName(name)
}

@RestController
@RequestMapping("/survey")
class SurveyController(private val service: Service) {

    @GetMapping("/all")
    fun getAllSurveyResponse()=service.findAllSurveyResponse()

    @GetMapping("/{id}")
    fun getSurveyResponseById(@PathVariable id: Long)=service.findBySurveyResponseId(id)
}
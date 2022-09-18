package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.ApiService
import com.wafflestudio.seminar.survey.service.ApiServiceImpl
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class ApiController(private val apiService: ApiService) {

    @GetMapping("/surveyResponse")
    fun getAllSurveyResponse():List<SurveyResponse>{
        return apiService.findAllSurveyResponse()
    }
    
    @GetMapping("/surveyResponse/{id}")
    fun getSurveyResponsebyId(@PathVariable("id") id:Long):SurveyResponse{
        return apiService.findSurveyResponsebyId(id) ?: throw NotFoundException("Cannot find SurveyResponse id: ${id}")
    }
    
    @GetMapping("/OS/{id}")
    fun getOSbyId(@PathVariable("id") id: Long):OperatingSystem{
        return apiService.findOSbyId(id) ?: throw NotFoundException("Cannot find OS id: ${id}")
    }
    
    @GetMapping("/OS")
    fun getOSbyName(@RequestParam name: String):OperatingSystem{
        return apiService.findOSbyName(name) ?: throw NotFoundException("Cannot find OS name: ${name}")
    }
    
}
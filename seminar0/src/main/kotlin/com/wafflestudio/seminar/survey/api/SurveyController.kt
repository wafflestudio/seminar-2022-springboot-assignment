package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SurveyController(
    private val req: SurveyResponseRepository,
) {
    
    @GetMapping("/all")
    fun getSurvey(): List<SurveyResponse>{
        return req.findAll()
    }
    
    @GetMapping("/id")
    fun getSurveyByID(id: Long): SurveyResponse{
        return req.findById(id)
    }
}
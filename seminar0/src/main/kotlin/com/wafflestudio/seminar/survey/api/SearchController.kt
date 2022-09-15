package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SearchService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class SearchController(
    private val searchService: SearchService
) {
    
    @GetMapping("/os/{id}")
    fun findOsById(@PathVariable id:Long):OperatingSystem {
        return searchService.getOsById(id) 
    }
    
    @GetMapping("/os")
    fun findOsByName(@RequestParam name:String):OperatingSystem {
        return searchService.getOsByName(name) 
    }
    
    @GetMapping("/survey-list")
    fun getSurveyResponse():List<SurveyResponse> {
        return searchService.getAllSurveyResponses()
    }
    
    @GetMapping("/survey/{id}")
    fun findSurveyResponse(@PathVariable id:Long): SurveyResponse {
        return searchService.getSurveyResponseById(id) 
    }
    
}


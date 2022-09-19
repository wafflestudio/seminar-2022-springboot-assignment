package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.MemoryDbService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val memoryDbService: MemoryDbService
) {
    // ...
    // 어떻게 API 연결할 수 있을까?
    @GetMapping("/survey-response-all")
    fun getAllSurveyResponses():List<SurveyResponse>
    {
        return memoryDbService.getAllSurveyResponses()
    }
    
    @GetMapping("/survey-response-id/{id}")
    fun getSurveyResponseById(@PathVariable id: Long):SurveyResponse
    {
        return memoryDbService.getSurveyResponseById(id)
    }
    
    @GetMapping("/os")
    fun getOs():List<OperatingSystem>
    {
        return memoryDbService.getAllOs()
    }

    @GetMapping("/os-name")
    fun getOsByName(@RequestParam name: String? = null) : OperatingSystem
    {
        val osName = name ?: ""
        return memoryDbService.getOsByName(osName)
    }

    @GetMapping("/os-id/{id}")
    fun getOsById(@PathVariable id: Long): OperatingSystem
    {
        return memoryDbService.getOsById(id)
    }
}
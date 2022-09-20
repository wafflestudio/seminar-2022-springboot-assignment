package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey-responses")
class SurveyResponseController(
    val surveyResponseService: SurveyResponseService
) {
    // 설문 결과 전체 보기 API
    @GetMapping("")
    fun findAll(): List<SurveyResponse> {
        return surveyResponseService.findAll()
    }
    
    // 설문 결과 ID로 검색 API
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): SurveyResponse {
        return surveyResponseService.findById(id)
    }
}
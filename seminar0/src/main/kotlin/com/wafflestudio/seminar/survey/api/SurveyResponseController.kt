package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class SurveyResponseController(
    val surveyResponseService: SurveyResponseService
) {
    // 설문 결과 전체 보기 API
    @GetMapping("/survey-responses")
    fun surveyResponseAll(): List<SurveyResponse> {
        TODO("Not yet implemented")
    }
    
    // 설문 결과 ID로 검색 API
    @GetMapping("/survey-responses/{id}")
    fun surveyResponseById(@PathVariable("id") id: Long): List<SurveyResponse> {
        TODO("Not yet implemented")
    }
}
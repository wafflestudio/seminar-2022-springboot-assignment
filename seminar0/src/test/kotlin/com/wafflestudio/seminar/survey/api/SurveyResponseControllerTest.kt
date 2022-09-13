package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.APIException
import com.wafflestudio.seminar.survey.service.OsService
import com.wafflestudio.seminar.survey.service.SurveyResponseService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import kotlin.properties.Delegates


/*
Implement Test Code for study
 */

@WebMvcTest
class SurveyResponseControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var surveyResponseService: SurveyResponseService
    
    // without osService error occurred why?
    @MockBean
    lateinit var osService: OsService
    
    @DisplayName("[API][GET] 모든 survey 조회하기")
    @Test 
    fun givenSurveys_whenGeAllRequest_thenReturnSurveyResponses() {
        
        val surveyResponse1 : SurveyResponse = getSurveyResponseForTest(1, "MacOS", 1)
        val surveyResponse2 : SurveyResponse = getSurveyResponseForTest(2, "Linux", 3)
        
        given(surveyResponseService.getAllSurveyResponses())
            .willReturn(listOf(surveyResponse1, surveyResponse2))
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/surveys"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Int>(2)))
            .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].operatingSystem.osName").value("MacOS"))
                .andExpect(jsonPath("$.[0].programmingExp").value(1))
            .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].operatingSystem.osName").value("Linux"))
                .andExpect(jsonPath("$.[1].programmingExp").value(3))
        
    }
    
    @DisplayName("[API][GET]survey id로 survey 성공 조회")
    @Test
    fun givenExistingSurvey_whenGetRequest_thenReturnsSurveyResponse() {
        val id :Long = 1
        given(surveyResponseService.getSurveyResponseById(id))
            .willReturn(getSurveyResponseForTest(id, "MacOS", 1))
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/survey/$id"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.operatingSystem.id").value(1))
            .andExpect(jsonPath("$.operatingSystem.osName").value("MacOS"))
            .andExpect(jsonPath("$.grade").value("1학년"))
            .andDo(MockMvcResultHandlers.print())
    }
    
    @DisplayName("[API][GET] 잘못된 survey id로 조회 실패")
    @Test
    fun givenNonExistingSurvey_whenGetRequest_thenReturnsStatus400()  {
        val id: Long = 2
        given(surveyResponseService.getSurveyResponseById(id))
            .willThrow(APIException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/survey/$id")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
    }
    
    fun getSurveyResponseForTest(surveyId: Long, osName: String, programmingExp: Int) : SurveyResponse {
        var osId by Delegates.notNull<Long>()
        when(osName) {
            "MacOS" -> osId = 1
            "Linux" -> osId = 2
            "Windows" -> osId = 3
        }

        return SurveyResponse(
            id = surveyId,
            operatingSystem = OperatingSystem(
                id = osId, osName = osName, price = 300000, desc = "Most favorite OS of Seminar Instructors"
            ),
            springExp = 1,
            rdbExp = 1,
            programmingExp = programmingExp,
            major = "컴퓨터공학부 주전공",
            grade = "1학년",
            timestamp = LocalDateTime.now(),
            backendReason = null,
            waffleReason = null,
            somethingToSay = null
        )
    }
}
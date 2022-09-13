package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


/*
Implement Test Code for study
 */

@WebMvcTest
class OsControllerTest(@Autowired val mockMvc: MockMvc) {
    
    @MockBean
    lateinit var osService: OsService

    // without surveyResponseService error occurred
    @MockBean
    lateinit var surveyResponseService: SurveyResponseService

    @DisplayName("[API][GET]모든 os 성공 조회")
    @Test
    fun givenExistingOS_whenGetRequestAllOS_thenReturnAllOS() {
        given(osService.getAllOs())
            .willReturn(listOf(
                OperatingSystem(id=1, osName = "MacOS", price = 300000, desc = "Most favorite OS of Seminar Instructors"),
                OperatingSystem(2L, "Linux", 0L, "Linus Benedict Torvalds"),
                OperatingSystem(3L, "Windows", 0L, "Window..")
            ))
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/os-all"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Int>(3)))
            .andExpect(jsonPath("$.[0].osName").value("MacOS"))
            .andExpect(jsonPath("$.[1].osName").value("Linux"))
            .andExpect(jsonPath("$.[2].osName").value("Windows"))
    }

    @DisplayName("[API][GET]os id로 성공 조회")
    @Test
    fun givenExistingOS_whenGetRequestById_thenReturnsOs() {
        val id :Long = 1
        given(osService.getOsById(id))
            .willReturn(OperatingSystem(id=1, osName = "MacOS", price = 300000, desc = "Most favorite OS of Seminar Instructors"))
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/os/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.osName").value("MacOS"))
            .andExpect(jsonPath("$.price").value(300000))
            .andExpect(jsonPath("$.desc").value("Most favorite OS of Seminar Instructors"))
    }

    @DisplayName("[API][GET]os id로 실패 조회")
    @Test
    fun givenNonExistingOS_whenGetRequestByWeirdId_thenReturns400() {
        val id :Long = 100
        given(osService.getOsById(id))
            .willThrow(APIException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/os/$id"))
            .andExpect(status().isBadRequest)
    }

    @DisplayName("[API][GET]os 이름으로 성공 조회")
    @Test
    fun givenExistingOS_whenGetRequestByName_thenReturnsOs() {
        val osName: String = "MacOS"
        given(osService.getOsByName(osName))
            .willReturn(OperatingSystem(id=1, osName = "MacOS", price = 300000, desc = "Most favorite OS of Seminar Instructors"))
        
        val requestParam: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParam.set("name", osName)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/os").param("name", osName))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.osName").value("MacOS"))
            .andExpect(jsonPath("$.price").value(300000))
            .andExpect(jsonPath("$.desc").value("Most favorite OS of Seminar Instructors"))
    }

    @DisplayName("[API][GET]os name으로 실패 조회")
    @Test
    fun givenNonExistingOS_whenGetRequestByWeirdName_thenReturns400() {
        val osName : String = "Mac"
        given(osService.getOsByName(osName))
            .willThrow(APIException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/os").param("name", osName))
            .andExpect(status().isBadRequest)
    }

}
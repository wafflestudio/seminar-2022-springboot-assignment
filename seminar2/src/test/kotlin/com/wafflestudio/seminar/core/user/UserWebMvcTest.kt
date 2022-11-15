package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
internal class UserWebMvcTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    
    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `회원가입을 시도할 수 있다`() {
        // given
        given(userService.createUser(any())).willReturn(AuthToken("1234"))
        
        // when
        mockMvc
            .perform(
                post("/api/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        { 
                            "email": "wlgur7238@snu.ac.kr",
                            "username": "강지혁",
                            "password": "1234",
                            "role": "PARTICIPANT"
                        }
                    """.trimIndent())
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("1234"))
            .andDo(print())
    }

}
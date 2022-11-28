package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.UserRole
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
internal class SeminarWebMvcTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
) {

    @MockBean
    private lateinit var seminarService: SeminarService

    fun givenAuthToken(): String {
        userRepository.save(UserEntity("", "", ""))
        return "Bearer " + authTokenService.generateTokenByEmail("").accessToken
    }

    fun givenInstAuthToken(): String {
        userService.signUp(
            SignUpRequest(
                "inst@ructor.com",
                "instructor",
                "secret",
                UserRole.INSTRUCTOR,
            )
        )
        return "Bearer " + authTokenService.generateTokenByEmail("inst@ructor.com").accessToken
    }

    @BeforeEach
    fun cleanRepository() {
        userRepository.deleteAll()
    }

    @Test
    fun `세미나 개설 constraints`() {
        // given
        given(seminarService.createSeminar(any(), any())).willReturn(any())
        val token = givenInstAuthToken()
        val notInstructorToken = givenAuthToken()

        // when
        // successful
        mockMvc.perform(
            post("/api/v1/seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", eq(token))
                .content(
                    """
                        {
                            "name": "spring",
                            "capacity": 1,
                            "count": 1,
                            "time": "10:00"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)

        // not instructor
        mockMvc.perform(
            post("/api/v1/seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", notInstructorToken)
                .content(
                    """
                        {
                            "name": "spring",
                            "capacity": 1,
                            "count": 1,
                            "time": "10:00"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isForbidden)

        // capacity or count is not positive
        mockMvc.perform(
            post("/api/v1/seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(
                    """
                        {
                            "name": "spring",
                            "capacity": 0,
                            "count": 0,
                            "time": "10:00"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)

        // wrong date time format
        mockMvc.perform(
            post("/api/v1/seminar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(
                    """
                        {
                            "name": "spring",
                            "capacity": 1,
                            "count": 1,
                            "time": "10-00"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)
    }

}
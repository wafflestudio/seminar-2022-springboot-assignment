package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.UserInfo
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@AutoConfigureMockMvc
@SpringBootTest
internal class UserWebMvcTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
) {

    @MockBean
    private lateinit var userService: UserService

    @BeforeEach
    fun cleanRepository() {
        userRepository.deleteAll()
    }

    fun givenAuthToken(): String {
        userRepository.save(UserEntity("", "", ""))
        return "Bearer " + authTokenService.generateTokenByEmail("").accessToken
    }

    @Test
    fun `회원가입 constraints`() {
        // given
        given(userService.signUp(any())).willReturn(AuthToken("AUTH_TOKEN"))

        // when
        // successful
        mockMvc.perform(
            post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "email": "example@email.com",
                            "username": "name",
                            "password": "secret",
                            "role": "PARTICIPANT"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("AUTH_TOKEN"))

        // wrong email
        mockMvc.perform(
            post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "email": "wrong_email.com",
                            "username": "name",
                            "password": "secret",
                            "role": "PARTICIPANT"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)

        // empty name
        mockMvc.perform(
            post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "email": "wrong_email.com",
                            "username": "",
                            "password": "secret",
                            "role": "PARTICIPANT"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)

        // empty password
        mockMvc.perform(
            post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "email": "wrong_email.com",
                            "username": "name",
                            "password": "",
                            "role": "PARTICIPANT"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)

        // wrong role
        mockMvc.perform(
            post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "email": "wrong_email.com",
                            "username": "name",
                            "password": "secret",
                            "role": "PARTPANT"
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Authentication 확인`() {
        // given
        given(userService.getUserById(any())).willReturn(UserInfo(1, "", "", LocalDateTime.now(), LocalDateTime.now(), null, null))
        val token = givenAuthToken()
        val invalidToken = token + "dafdsafwfadsf"
        val notFoundToken = "Bearer " + authTokenService.generateTokenByEmail("non@exist.email").accessToken

        // when
        // auth success
        mockMvc.perform(
            get("/api/v1/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
            .andExpect(status().isOk)

        // no token
        mockMvc.perform(
            get("/api/v1/me")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)

        // invalid token
        mockMvc.perform(
            get("/api/v1/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", invalidToken)
        )
            .andExpect(status().isUnauthorized)

        // user not found token
        mockMvc.perform(
            get("/api/v1/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", notFoundToken)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `유저정보 수정 constraints`() {
        // given
        given(userService.updateUser(any(), any())).willReturn(UserInfo(1, "", "", LocalDateTime.now(), LocalDateTime.now(), null, null))
        val token = givenAuthToken()

        // when
        // negative year value
        mockMvc.perform(
            put("/api/v1/user/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(
                    """
                        {
                            "year": -1
                        }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)

    }

}
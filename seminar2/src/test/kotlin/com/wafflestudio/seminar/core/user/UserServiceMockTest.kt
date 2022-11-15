package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.times
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
internal class UserServiceMockTest @Autowired constructor(
    private val userService: UserService,
) {
    @MockBean
    private lateinit var authTokenService: AuthTokenService

    @Test
    fun `회원가입하면 인증토큰을 받아볼 수 있다`() {
        // given
        given(authTokenService.generateTokenByEmail(anyString())).willReturn(AuthToken("AUTHTOKEN"))
        val request = SignUpRequest("w@affle.com", "", "", SignUpRequest.Role.INSTRUCTOR)

        // when
        val result = userService.createUser(request)

        // then
        assertThat(result.accessToken).isEqualTo("AUTHTOKEN")
        Mockito.verify(authTokenService, times(1)).generateTokenByEmail("w@affle.com")
    }

}
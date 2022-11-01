package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
internal class UserServiceMockkTest @Autowired constructor(
    userRepository: UserRepository,
    passwordEncoder: PasswordEncoder,
) {

    private val mockk: AuthTokenService = mockk()
    private val userService2 = UserService(userRepository, mockk, passwordEncoder)

    @Test
    fun `회원가입하면 인증토큰을 받아볼 수 있다 2`() {
        // given
        every { mockk.generateTokenByEmail(any()) } returns AuthToken("AUTHTOKEN")
        val request = SignUpRequest("w@affle.com", "", "", SignUpRequest.Role.INSTRUCTOR)

        // when
        val result = userService2.createUser(request)

        // then
        assertThat(result.accessToken).isEqualTo("AUTHTOKEN")
        verify(exactly = 1) { mockk.generateTokenByEmail(any()) }
    }

}
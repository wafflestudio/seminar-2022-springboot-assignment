package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userTestHelper: UserTestHelper,
    private val userRepository: UserRepository,
) {

    /**
     * TODO : 각 테스트에서 유저가 "한 명"임을 검증하고 있다.
     *        테스트가 깨질까, 깨지지 않을까?
     *        깨진다면, 깨지지 않도록 어떻게 해야 할까?
     */

    @Test
    fun `테스트 1 - 유저를 만들 수 있다`() {
        // given
        val request = SignUpRequest("w@affle.com", "", "", SignUpRequest.Role.INSTRUCTOR)

        // when
        userService.createUser(request)

        // then
        assertThat(userRepository.findAll()).isEqualTo(1)
    }

    @Test
    fun `테스트 2 - 유저를 만들고 로그인 할 수 있다`() {
        // given
        userTestHelper.createUser("waffle@studio.com", password = "1234")
        val request = SignInRequest("waffle@studio.com", "1234")

        // when
        val result = userService.logIn(request.email, request.password)

        // then
        assertThat(result.accessToken).isNotEmpty
        assertThat(userRepository.findAll()).hasSize(1)
    }

}
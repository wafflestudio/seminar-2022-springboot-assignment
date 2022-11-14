package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantEnrollRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.UserRole
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.anyString
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus

@SpringBootTest
internal class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userTestHelper: UserTestHelper,
    private val userRepository: UserRepository,
) {
    @MockBean
    private lateinit var authTokenService: AuthTokenService

    private val email = "example@email.com"
    private val password = "secret"

    @BeforeEach
    fun cleanRepository() {
        userRepository.deleteAll()
    }

    fun givenMockToken() {
        given(authTokenService.generateTokenByEmail(anyString())).willReturn(AuthToken("AUTH_TOKEN"))
    }

    fun givenDefaultCreatedParticipant() {
        userTestHelper.createParticipant(email, "default", password)
    }

    fun givenDefaultCreatedInstructor() {
        userTestHelper.createInstructor(email, "default", password)
    }

    @Test
    fun `회원가입 성공`() {
        // given
        givenMockToken()
        val request = SignUpRequest(email, "", "", UserRole.PARTICIPANT, null, true, null, null)

        // when
        val result = userService.signUp(request)

        // then
        assertThat(result.accessToken).isEqualTo("AUTH_TOKEN")
        assertThat(userRepository.findAll()).hasSize(1)
        assertThat(userRepository.findByEmail(email)).isNotNull
    }

    @Test
    fun `회원가입 실패 - 중복 이메일`() {
        // given
        givenDefaultCreatedParticipant()
        val request = SignUpRequest(email, "", "", UserRole.PARTICIPANT, null, true, null, null)

        // when
        val exception = assertThrows<SeminarException> {
            userService.signUp(request)
        }

        // then
        assertThat(exception.status).isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `로그인 성공`() {
        // given
        givenMockToken()
        givenDefaultCreatedParticipant()
        val request = LogInRequest(email, password)

        // when
        val result = userService.logIn(request)

        // then
        assertThat(result.accessToken).isEqualTo("AUTH_TOKEN")
    }

    @Test
    fun `로그인 실패 - 잘못된 비밀번호`() {
        // given
        givenMockToken()
        givenDefaultCreatedParticipant()
        val request = LogInRequest(email, "")

        // when
        val exception = assertThrows<SeminarException> {
            userService.logIn(request)
        }

        // then
        assertThat(exception.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `유저 정보 조회 성공`() {
        // given
        givenDefaultCreatedParticipant()
        val userId = userRepository.findByEmail(email)!!.id

        // when
        val result = userService.getUserById(userId)

        // then
        assertThat(result.email).isEqualTo(email)
    }

    @Test
    fun `유저 정보 조회 실패 - 존재하지 않는 유저`() {
        // given
        givenDefaultCreatedParticipant()

        // when
        val exception = assertThrows<SeminarException> {
            userService.getUserById(0)
        }

        // then
        assertThat(exception.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `유저 정보 수정 성공`() {
        // given
        givenDefaultCreatedParticipant()
        val userId = userRepository.findByEmail(email)!!.id
        val university = "university"
        val name = "name"
        val request = UpdateRequest(name,null, university, null, null)

        // when
        val result = userService.updateUser(userId, request)

        // then
        assertThat(result.username).isEqualTo(name)
        assertThat(result.participant).isNotNull
        assertThat(result.participant!!.university).isEqualTo(university)
        assertThat(result.instructor).isNull()
    }
    
    @Test
    fun `진행자가 수강생으로 등록 성공`() {
        // given
        givenDefaultCreatedInstructor()
        val userId = userRepository.findByEmail(email)!!.id
        val request = ParticipantEnrollRequest(null, true)

        // when
        val result = userService.participantEnroll(userId, request)

        // then
        assertThat(result.participant).isNotNull
    }
}
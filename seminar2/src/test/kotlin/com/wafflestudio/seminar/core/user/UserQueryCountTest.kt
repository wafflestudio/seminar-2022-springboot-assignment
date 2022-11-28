package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantEnrollRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.UserRole
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
internal class UserQueryCountTest @Autowired constructor(
    private val hibernateQueryCounter: HibernateQueryCounter,
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
        given(authTokenService.generateTokenByEmail(BDDMockito.anyString())).willReturn(AuthToken("AUTH_TOKEN"))
    }

    fun givenDefaultCreatedParticipant() {
        userTestHelper.createParticipant(email, "default", password)
    }

    fun givenDefaultCreatedInstructor() {
        userTestHelper.createInstructor(email, "default", password)
    }

    @Test
    fun `signUp() Query Count`() {
        // given
        givenMockToken()
        val request = SignUpRequest(email, "", "", UserRole.PARTICIPANT, null, true, null, null)

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            userService.signUp(request)
        }

        // then
        assertThat(result.accessToken).isEqualTo("AUTH_TOKEN")
        assertThat(userRepository.findAll()).hasSize(1)
        assertThat(userRepository.findByEmail(email)).isNotNull
        assertThat(queryCount).isEqualTo(2)
    }

    @Test
    fun `login() Query Count`() {
        // given
        givenMockToken()
        givenDefaultCreatedParticipant()
        val request = LogInRequest(email, password)

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            userService.logIn(request)
        }

        // then
        assertThat(result.accessToken).isEqualTo("AUTH_TOKEN")
        assertThat(queryCount).isEqualTo(1)
    }

    @Test
    fun `getUserById() Query Count`() {
        // given
        givenDefaultCreatedParticipant()
        val userId = userRepository.findByEmail(email)!!.id

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            userService.getUserById(userId)
        }

        // then
        assertThat(result.email).isEqualTo(email)
        assertThat(queryCount).isEqualTo(1)
    }
    
    @Test
    fun `updateUser() Query Count`() {
        givenDefaultCreatedParticipant()
        val userId = userRepository.findByEmail(email)!!.id
        val university = "university"
        val name = "name"
        val request = UpdateRequest(name,null, university, null, null)

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            userService.updateUser(userId, request)
        }

        // then
        assertThat(result.username).isEqualTo(name)
        assertThat(result.participant).isNotNull
        assertThat(result.participant!!.university).isEqualTo(university)
        assertThat(result.instructor).isNull()
        assertThat(queryCount).isEqualTo(3)
    }
    
    @Test
    fun `participantEnroll() Query Count`() {
        // given
        givenDefaultCreatedInstructor()
        val userId = userRepository.findByEmail(email)!!.id
        val request = ParticipantEnrollRequest(null, true)

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            userService.participantEnroll(userId, request)
        }

        // then
        assertThat(result.participant).isNotNull
        assertThat(queryCount).isEqualTo(3)
    }

}
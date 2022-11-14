package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.maptable.SeminarUserRepository
import com.wafflestudio.seminar.core.seminar.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.seminar.api.request.createSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.api.request.RegisterSeminarRequest
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val userTestHelper: UserTestHelper,
    private val seminarTestHelper: SeminarTestHelper,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val seminarUserRepository: SeminarUserRepository,
) {
    @MockBean
    private lateinit var authTokenService: AuthTokenService

    private val email = "example@email.com"

    private val seminarName = "seminarname"

    private val httpServletRequest = mock(HttpServletRequest::class.java)

    @BeforeEach
    fun cleanRepository() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
    }

    fun givenDefaultCreatedParticipant() {
        userTestHelper.createParticipant(email)
    }

    fun givenDefaultCreatedInstructor() {
        userTestHelper.createInstructor(email)
    }

    fun givenDefaultCreatedSeminar(): Long {
        val seminar =  seminarTestHelper.createSeminar(name = seminarName, instructor = userTestHelper.createInstructor(email))
        return seminar.id
    }

    fun givenServletRequestReturnsEmail() {
        given(httpServletRequest.getAttribute("email")).willReturn(email)
    }

    // FIXME: 유저 관련 API는 유저 도메인에 있는 게 더 좋을 것 같습니다!
    @Test
    @Transactional
    fun `진행자가 수강생으로 등록 성공`() {
        // given
        givenDefaultCreatedInstructor()
        val request = RegisterParticipantRequest(null, null)
        givenServletRequestReturnsEmail()

        // when
        val result = seminarService.registerToParticipant(httpServletRequest, request)

        // then
        assertThat(result.participant).isNotNull
    }

    // FIXME: Internal Server Error 발생, SeminarUserRepository에 저장하지 않아서 그런 것 같습니다!
    @Test
    @Transactional
    fun `세미나 개설 성공`() {
        // given
        givenDefaultCreatedInstructor()
        val request = createSeminarRequest(seminarName, 40, 6, "10:00")
        givenServletRequestReturnsEmail()

        // when
        val result = seminarService.createSeminar(httpServletRequest, request)

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(seminarRepository.findAll()).hasSize(1)
        assertThat(result.instructors).hasSize(1)
    }

    // TODO: 음수인 경우 에러처리 필요
    @Test
    @Transactional
    fun `세미나 개설 실패 - capacity 혹은 count 값 음수`() {
        // given
        givenDefaultCreatedInstructor()
        val request1 = createSeminarRequest(seminarName, -40, 6, "10:00")
        val request2 = createSeminarRequest(seminarName, 40, -6, "10:00")
        givenServletRequestReturnsEmail()

        // when
        val exception1 = assertThrows<SeminarException> {
            seminarService.createSeminar(httpServletRequest, request1)
        }
        val exception2 = assertThrows<SeminarException> {
            seminarService.createSeminar(httpServletRequest, request2)
        }

        // then
        assertThat(exception1.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception2.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    // FIXME: Handle 되지 않아 Internal error로 발생
    @Test
    @Transactional
    fun `세미나 개설 실패 - 유효하지 않은 시간 형식`() {
        // given
        givenDefaultCreatedInstructor()
        val request = createSeminarRequest(seminarName, 40, 6, "1000")
        givenServletRequestReturnsEmail()

        // when
        val exception = assertThrows<SeminarException> {
            seminarService.createSeminar(httpServletRequest, request)
        }

        // then
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Transactional
    fun `세미나 정보 조회 성공`() {
        // given
        givenDefaultCreatedSeminar()

        // when
        val result = seminarService.getSeminar(httpServletRequest, 1L)

        // then
        assertThat(result.name).isEqualTo(seminarName)
    }

    @Test
    @Transactional
    fun `여러 세미나 정보 조회 성공`() {
        // given
        val instructors = (0..9).map {
            userTestHelper.createInstructor("inst$it@ructor.com")
        }

        val seminars1 = (0..6).map {
            seminarTestHelper.createSeminar(name = "SpringSeminar", instructor = instructors[it])
        }
        val seminars2 = (7..9).map {
            seminarTestHelper.createSeminar(name = "DjangoSeminar", instructor = instructors[it])
        }

        // when
        val result1 = seminarService.getQuerySeminar(httpServletRequest, "", "Spring")
        val result2 = seminarService.getQuerySeminar(httpServletRequest, "", "Django")
        val result3 = seminarService.getQuerySeminar(httpServletRequest, "", "Seminar")
        val result4 = seminarService.getQuerySeminar(httpServletRequest, "earliest", "")

        // then
        assertThat(result1).hasSize(7)
        assertThat(result2).hasSize(3)
        assertThat(result3).hasSize(10)
        (0..9).forEach {
            assertThat(result3[it]).isEqualTo(result4[9 - it])
        }
    }

    // FIXME: 수강생이 수강해도 empty list를 반환
    // FIXME: 에러 발생, com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.wafflestudio.seminar.core.seminar.api.response.CreateSeminarResponse["instructors"]->java.util.ArrayList[0]->com.wafflestudio.seminar.core.user.database.UserEntity$HibernateProxy$6m9fNoL0["hibernateLazyInitializer"])
    @Test
    @Transactional
    fun `세미나 참여하기`() {
        // given
        val seminarId = givenDefaultCreatedSeminar()
        val email2 = "another@email.com"
        userTestHelper.createParticipant(email2)
        given(httpServletRequest.getAttribute("email")).willReturn(email2)
        val request = RegisterSeminarRequest("participants")

        // when
        val result = seminarService.registerSeminar(httpServletRequest, seminarId, request)

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(result.participants).isNotNull
        assertThat(result.participants).hasSize(1)
    }

    // FIXME: CreateSeminarResponse에서 유저 정보가 파싱되지 않고 user entity raw data로 반환 
    @Test
    @Transactional
    fun `세미나 함께 진행하기`() {
        // given
        val seminarId = givenDefaultCreatedSeminar()
        val email2 = "another@email.com"
        userTestHelper.createInstructor(email2)
        given(httpServletRequest.getAttribute("email")).willReturn(email2)
        val request = RegisterSeminarRequest("instructors")

        // when
        val result = seminarService.registerSeminar(httpServletRequest, seminarId, request)

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(result.instructors).hasSize(2)
    }

    // FIXME: 에러 발생 java.lang.IllegalStateException: stream has already been operated upon or closed
    @Test
    @Transactional
    fun `세미나 드랍하기`() {
        // given
        val seminarId = givenDefaultCreatedSeminar()
        val email2 = "anothre@email.com"
        val user = userTestHelper.createParticipant(email2)
        given(httpServletRequest.getAttribute("email")).willReturn(email2)

        val participantRequest = RegisterSeminarRequest("participants")
        seminarService.registerSeminar(httpServletRequest, seminarId, participantRequest)

        // when
        val result = seminarService.dropSeminar(httpServletRequest, seminarId)
        val exception = assertThrows<SeminarException> {
            seminarService.registerSeminar(httpServletRequest, seminarId, participantRequest)
        }

        val seminar = seminarRepository.findByIdOrNull(seminarId)!!
        val seminarUser = seminarUserRepository.findByUserAndSeminar(user, seminar)

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(seminarUser[0].isActive).isEqualTo(false)
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.participants).hasSize(1)
    }

}
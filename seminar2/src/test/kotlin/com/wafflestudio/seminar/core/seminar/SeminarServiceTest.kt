package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.InstructorSeminarTableRepository
import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val userTestHelper: UserTestHelper,
    private val seminarTestHelper: SeminarTestHelper,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val participantSeminarTableRepository: ParticipantSeminarTableRepository,
    private val instructorSeminarTableRepository: InstructorSeminarTableRepository,
) {
    @MockBean
    private lateinit var authTokenService: AuthTokenService

    private val email = "example@email.com"

    private val seminarName = "seminarname"

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

    @Test
    fun `세미나 개설 성공`() {
        // given
        givenDefaultCreatedInstructor()
        val userId = userRepository.findByEmail(email)!!.id
        val request = CreateSeminarRequest(seminarName, 40, 6, LocalTime.now())

        // when
        val result = seminarService.createSeminar(userId, request)

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(seminarRepository.findAll()).hasSize(1)
        assertThat(result.instructors).hasSize(1)
    }

    @Test
    fun `세미나 개설 실패 - capacity 혹은 count 값 음수`() {
        // given
        givenDefaultCreatedInstructor()
        val userId = userRepository.findByEmail(email)!!.id
        val request1 = CreateSeminarRequest(seminarName, -40, 6, LocalTime.now())
        val request2 = CreateSeminarRequest(seminarName, 40, -6, LocalTime.now())

        // when
        val exception1 = assertThrows<SeminarException> {
            seminarService.createSeminar(userId, request1)
        }
        val exception2 = assertThrows<SeminarException> {
            seminarService.createSeminar(userId, request2)
        }

        // then
        assertThat(exception1.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception2.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `세미나 개설 실패 - 유효하지 않은 시간 형식`() {
        // given
        givenDefaultCreatedInstructor()
        val userId = userRepository.findByEmail(email)!!.id
        val request = CreateSeminarRequest(seminarName, 40, 6, LocalTime.now())

        // when
        val exception = assertThrows<SeminarException> {
            seminarService.createSeminar(userId, request)
        }

        // then
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `세미나 정보 조회 성공`() {
        // given
        givenDefaultCreatedSeminar()

        // when
        val result = seminarService.getSeminarById(1L)

        // then
        assertThat(result.name).isEqualTo(seminarName)
    }

    @Test
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
        val result2 = seminarService.getSeminarOption("", "Django")
        val result3 = seminarService.getSeminarOption("", "Seminar")
        val result4 = seminarService.getSeminarOption("earliest", "")
        val result1 = seminarService.getSeminarOption("", "Spring")

        // then
        assertThat(result1).hasSize(7)
        assertThat(result2).hasSize(3)
        assertThat(result3).hasSize(10)
        (0..9).forEach {
            assertThat(result3[it]).isEqualTo(result4[9 - it])
        }
    }

    @Test
    fun `세미나 참여하기`() {
        // given
        val seminarId = givenDefaultCreatedSeminar()
        val email2 = "another@email.com"
        val userId = userTestHelper.createParticipant(email2).id

        // when
        val result = seminarService.participateSeminar(userId, seminarId)
        
        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(result.participants).isNotNull
        assertThat(result.participants).hasSize(1)
    }

    @Test
    fun `세미나 함께 진행하기`() {
        // given
        val seminarId = givenDefaultCreatedSeminar()
        val email2 = "another@email.com"
        val userId = userTestHelper.createInstructor(email2).id

        // when
        val result = seminarService.instructSeminar(userId, seminarId)

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(result.instructors).hasSize(2)
    }

    @Test
    fun `세미나 드랍하기`() {
        // given
        val seminarId = givenDefaultCreatedSeminar()
        val email2 = "anothre@email.com"
        val user = userTestHelper.createParticipant(email2)
        val userId = user.id

        seminarService.participateSeminar(userId, seminarId)

        // when
        val result = seminarService.dropSeminar(userId, seminarId)
        val exception = assertThrows<SeminarException> {
            seminarService.participateSeminar(userId, seminarId)
        }

        val seminar = seminarRepository.findByIdOrNull(seminarId)!!
        val table = seminar.participantSet.first()

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(table.isActive).isEqualTo(false)
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.participants).hasSize(1)
    }

}
package com.wafflestudio.seminar.core.user.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.global.HibernateQueryCounter
import io.mockk.every
import io.mockk.mockk
import org.mockito.BDDMockito.anyString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val userRepository: UserRepository,
    private val queryFactory: JPAQueryFactory,
    private val seminarService: SeminarService
){
    @MockBean
    private lateinit var authTokenService: AuthTokenService

    @BeforeEach
    fun provideUserRepository() {
        userRepository.deleteAll()
        val instructorProfileEntity = InstructorProfileEntity("waffle", 3)
        val instructor = UserEntity("name", "email@snu.ac.kr", "1234", dateJoined = LocalDate.now(), instructor =  instructorProfileEntity)
        userRepository.save(instructor)
    }

    /*
    * createSeminar()
    */

    // Failed: [N+1]
    @Test
    fun `세미나 생성`() {
        // Given
        given(authTokenService.getCurrentEmail(anyString())).willReturn("email@snu.ac.kr")
        val seminarRequest = SeminarRequest("spring", 30, 6, "19:00", false)
        
        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.createSeminar(seminarRequest, "token")
        }
        
        // Then
        assertThat(response.name).isEqualTo("spring")
        assertThat(queryCount).isEqualTo(8) // [N+1] but was 10
    }

    // Passed
    @Test
    fun `request body에 필수 정보가 누락되면 400으로 응답`() {
        // Given
        given(authTokenService.getCurrentEmail(anyString())).willReturn("email@snu.ac.kr")
        val seminarRequest = SeminarRequest(null, 30, 6, "19:00", false)

        // When
        val response = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest, "token") }

        // Then
        assertThat(response.message).isEqualTo("입력하지 않은 값이 있습니다")
    }

    // Passed
    @Test
    fun `name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답`() {
        // Given
        given(authTokenService.getCurrentEmail(anyString())).willReturn("email@snu.ac.kr")
        val seminarRequest1 = SeminarRequest("", 30, 6, "19:00", false)
        val seminarRequest2 = SeminarRequest("spring", 0, 6, "19:00", false)
        val seminarRequest3 = SeminarRequest("spring", 30, -2, "19:00", false)

        // When
        val response1 = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest1, "token") }
        val response2 = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest2, "token") }
        val response3 = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest3, "token") }

        // Then
        assertThat(response1.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
        assertThat(response2.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
        assertThat(response3.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
    }

    // Passed
    @Test
    fun `진행자 자격이 아닌 User가 요청하면 403으로 응답`() {
        // Given
        given(authTokenService.getCurrentEmail(anyString())).willReturn("email@snu.ac.kr")
        userRepository.deleteAll()
        val participantProfileEntity = ParticipantProfileEntity("snu", true)
        val participant = UserEntity("name", "email@snu.ac.kr", "1234", dateJoined = LocalDate.now(), participant =  participantProfileEntity)
        userRepository.save(participant)
        val seminarRequest = SeminarRequest("spring", 30, 6, "19:00", false)

        // When
        val response = assertThrows<Seminar403> { seminarService.createSeminar(seminarRequest, "token") }

        // Then
        assertThat(response.message).isEqualTo("진행자만 세미나를 생성할 수 있습니다")
    }

    /*
    * updateSeminar
    */

    @Test
    fun updateSeminar() {
        // Given

        // When

        // Then
    }

    /*
    * getSeminarById
    */

    @Test
    fun getSeminarById() {
        // Given

        // When

        // Then
    }

    /*
    * getSeminars
    */

    @Test
    fun getSeminars() {
        // Given

        // When

        // Then
    }

    /*
    * getSeminarByName
    */

    @Test
    fun getSeminarByName() {
        // Given

        // When

        // Then
    }

    /*
    * joinSeminar
    */

    @Test
    fun joinSeminar() {
        // Given

        // When

        // Then
    }

    /*
    * dropSeminar
    */

    @Test
    fun dropSeminar() {
        // Given

        // When

        // Then
    }
}
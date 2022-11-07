package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.global.HibernateQueryCounter
import com.wafflestudio.seminar.core.global.TestHelper
import com.wafflestudio.seminar.core.seminar.api.request.ParticipateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.beans.factory.annotation.Autowired
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val seminarRepository: SeminarRepository,
    private val testHelper: TestHelper,
    private val userSeminarRepository: UserSeminarRepository,
) {

    @Test
    @Transactional
    fun `테스트 1 - 세미나를 생성할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "email.com")
        val request = SeminarRequest("seminar", 100, 5, "12:30")

        // when
        seminarService.createSeminar(instructor, request)

        // then
        assertThat(seminarRepository.findAll()).hasSize(1)
    }

    @Test
    @Transactional
    fun `테스트 2 - 세미나를 수정할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "email.com")
        val seminar = testHelper.createSeminar(name = "seminar", userEntity = instructor)
        val request = UpdateSeminarRequest(
            id = seminar.id,
            name = "seminar2",
            capacity = 200,
            count = 20,
            time = "12:30",
            online = false
        )

        // when
        seminarService.updateSeminar(instructor, request)

        // then
        assertThat(seminarRepository.findByName(request.name!!)).isNotEmpty
        assertEquals(seminarRepository.findByName(request.name!!).get().capacity, request.capacity)
        assertEquals(seminarRepository.findByName(request.name!!).get().count, request.count)
        assertEquals(seminarRepository.findByName(request.name!!).get().online, request.online)
    }

    @Test
    @Transactional
    fun `테스트 3_1 - 세미나에 참여할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "email.com")
        val seminar = testHelper.createSeminar(name = "seminar", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "email2.com")
        val request = ParticipateSeminarRequest(participant.role)

        // when
        seminarService.participateSeminar(seminar.id, participant, request)

        // then
        assertThat(userSeminarRepository.findUserSeminarByUserIdAndSeminarId(participant.id, seminar.id)).isNotNull
    }

    @Test
    fun `테스트 3_2 - 세미나를 함께 진행할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "email.com")
        val seminar = testHelper.createSeminar(name = "seminar", userEntity = instructor)
        val instructor2 = testHelper.createParticipant(email = "email2.com")
        val request = ParticipateSeminarRequest(instructor2.role)

        // when
        seminarService.participateSeminar(seminar.id, instructor2, request)

        // then
        assertThat(userSeminarRepository.findUserSeminarByUserIdAndSeminarId(instructor2.id, seminar.id))

    }

    @Test
    fun `테스트 4 - 세미나를 드랍할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "email.com")
        val seminar = testHelper.createSeminar(name = "seminar", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "email2.com")
        testHelper.createUserSeminar(participant, seminar)

        // when
        seminarService.dropSeminar(participant, seminar.id)
        val userSeminar = userSeminarRepository.findUserSeminarByUserIdAndSeminarId(participant.id, seminar.id)

        // then
        assertThat(userSeminar).isNotNull
        assertEquals(userSeminar!!.isActive, false)
        assertThat(userSeminar!!.droppedAt).isNotNull

    }

    @Test
    fun `테스트 5 - 세미나를 없앨 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "email.com")
        val seminar = testHelper.createSeminar(name = "seminar", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "email2.com")
        testHelper.createUserSeminar(participant, seminar)

        // when
        seminarService.deleteSeminar(instructor, seminar.id)

        // then
        assertThat(seminarRepository.findById(seminar.id)).isEmpty
        assertThat(userSeminarRepository.findUserSeminarByUserIdAndSeminarId(participant.id, seminar.id)).isNull()
    }

    @Test
    fun `테스트 6 - 세미나 id를 통해 세미나를 불러올 수 있다`() {
        // given
        val seminar = createFixtures()

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminar(seminar.id)
        }

        // then
        assertThat(queryCount).isEqualTo(2)
    }

    @Test
    fun `테스트 7 - 특정 이름에 맞는 세미나를 원하는 순서로 불러올 수 있다`() {
        // given
        val seminar = createFixtures()

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = seminar.name, "earliest", 0, 20)
        }

        // then
        assertThat(result.content.size).isEqualTo(1)
        assertThat(queryCount).isEqualTo(1)

    }

    private fun createFixtures(): SeminarEntity {
        val instructor = testHelper.createInstructor(email = "instructor1.com")
        val participants = (1..5).map { testHelper.createParticipant(email = "participant#$it.com") }
        val seminar = testHelper.createSeminar(name = "seminarForTest", userEntity = instructor)
        participants.map { testHelper.createUserSeminar(it, seminar) }

        val instructor2 = testHelper.createInstructor(email = "instructor2.com")
        val participants2 = (6..10).map { testHelper.createParticipant(email = "participant#$it.com") }
        val seminar2 = testHelper.createSeminar(name = "seminarForTest2", userEntity = instructor2)
        participants2.map { testHelper.createUserSeminar(it, seminar2)}

        return seminar
    }
}
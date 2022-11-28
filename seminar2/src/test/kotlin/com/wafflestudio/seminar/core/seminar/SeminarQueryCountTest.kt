package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableEntity
import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

@SpringBootTest
internal class SeminarQueryCountTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val participantSeminarTableRepository: ParticipantSeminarTableRepository,
    private val userTestHelper: UserTestHelper,
    private val seminarTestHelper: SeminarTestHelper,
) {

    private val email = "example@email.com"
    private val seminarName = "seminarname"

    @BeforeEach
    fun cleanRepository() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
    }

    fun givenDefaultCreatedInstructor() {
        userTestHelper.createInstructor(email)
    }

    @Transactional
    fun prepareSeminarList(): Pair<Long, Long> {
        val instructor = userTestHelper.createInstructor("inst@ructor.com")
        val participants = (0..9).map {
            userTestHelper.createParticipant("part$it@icipant.com")
        }

        val seminar = seminarTestHelper.createSeminar(instructor = instructor, capacity = 20)

        participants.forEach {
            val table = participantSeminarTableRepository.save(
                ParticipantSeminarTableEntity(
                    it,
                    seminar,
                    true,
                    null,
                )
            )
        }

        return Pair(instructor.id, seminar.id)
    }

    @Test
    fun `createSeminar() Query Count`() {
        // given
        givenDefaultCreatedInstructor()
        val userId = userRepository.findByEmail(email)!!.id
        val request = CreateSeminarRequest(seminarName, 40, 6, LocalTime.now())

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.createSeminar(userId, request)
        }

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(queryCount).isEqualTo(3)
    }

    @Test
    fun `updateSeminar() Query Count`() {
        // given
        val (instructorId, seminarId) = prepareSeminarList()
        val request = UpdateSeminarRequest(seminarId, seminarName, null, null, null)

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.updateSeminar(instructorId, request)
        }

        // then
        assertThat(result.name).isEqualTo(seminarName)
        assertThat(queryCount).isEqualTo(4)
    }

    @Test
    fun `getSeminarById() Query Count`() {
        // given
        val seminarId = prepareSeminarList().second

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarById(seminarId)
        }

        // then
        assertThat(result.name).isEqualTo("")
        assertThat(queryCount).isEqualTo(3)
    }

    @Test
    fun `participateSeminar() Query Count`() {
        // given
        val seminarId = prepareSeminarList().second
        val email2 = "another@email.com"
        val userId = userTestHelper.createParticipant(email2).id

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.participateSeminar(userId, seminarId)
        }

        // then
        assertThat(result.name).isEqualTo("")
        assertThat(result.participants).isNotNull
        assertThat(result.participants).hasSize(11)
        assertThat(queryCount).isEqualTo(4)
    }

    @Test
    fun `instructSeminar() Query Count`() {
        // given
        val seminarId = prepareSeminarList().second
        val email2 = "another@email.com"
        val userId = userTestHelper.createInstructor(email2).id

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.instructSeminar(userId, seminarId)
        }

        // then
        assertThat(result.name).isEqualTo("")
        assertThat(result.instructors).hasSize(2)
        assertThat(queryCount).isEqualTo(4)
    }

    @Test
    fun `dropSeminar() Query Count`() {
        // given
        val seminarId = prepareSeminarList().second
        val email2 = "another@email.com"
        val user = userTestHelper.createParticipant(email2)
        val userId = user.id

        seminarService.participateSeminar(userId, seminarId)

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.dropSeminar(userId, seminarId)
        }

        val table = participantSeminarTableRepository.findAll().find {
            it.participant.id == userId
        }

        // then
        assertThat(result.name).isEqualTo("")
        assertThat(table).isNotNull
        assertThat(table!!.participant.id).isEqualTo(userId)
        assertThat(table.isActive).isEqualTo(false)
        assertThat(result.participants).hasSize(11)
        assertThat(queryCount).isEqualTo(5)
    }

    @Test
    fun `여러 세미나 조회 Query Count`() {
        // given
        prepareSeminarList()

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarOption(null, null)
        }

        // then
        assertThat(result).hasSize(1)
        assertThat(queryCount).isEqualTo(3)
    }
}
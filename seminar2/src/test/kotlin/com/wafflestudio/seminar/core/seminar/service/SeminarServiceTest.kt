package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.global.HibernateQueryCounter
import com.wafflestudio.seminar.core.global.TestHelper
import com.wafflestudio.seminar.core.seminar.api.request.ParticipateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.beans.factory.annotation.Autowired
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
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
//        assertThat(seminarRepository.findByName(request.name!!)).isNotEmpty
        assertEquals(seminarRepository.findByName(request.name!!)!!.capacity, request.capacity)
        assertEquals(seminarRepository.findByName(request.name!!)!!.count, request.count)
        assertEquals(seminarRepository.findByName(request.name!!)!!.online, request.online)
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
    @Transactional
    fun `테스트 6 - 세미나 id를 통해 세미나를 불러오기 성공`() {
        // given
        val seminar = createFixtures()

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminar(seminar.id)
        }
        assertThat(queryCount).isEqualTo(1) // TODO!: 질문! query count가 2 여야 될 것 같은데 오히려 1이 나옴
        assertThat(result.id).isEqualTo(seminar.id)
    }

    @Test
    @Transactional
    fun `테스트 7 - 특정 이름에 맞는 세미나를 원하는 순서로 불러오기 성공`() {
        // given
        createSeminar("test7-1@gmail.com", "Spring1", 3)
        createSeminar("test7-2@gmail.com", "Spring2", 2)
        createSeminar("test7-3@gmail.com", "Spring3", 2)
        createSeminar("test7-4@gmail.com", "Django", 3)
        createSeminar("test7-5@gmail.com", "iOS", 5)
        createSeminar("test7-6@gmail.com", "iOS2", 5)


        // when
        val (result1, queryCount1) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = "pring", "earliest", 0, 20)
        }

        val (result2, queryCount2) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = "pring", null, 0, 20)
        }

        val (result3, queryCount3) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = "Django", null, 0, 20)
        }
        
        // then
        assertThat(result1.content.size).isEqualTo(3)
        assertThat(result1.content.get(0).name).isEqualTo("Spring1")
        assertThat(queryCount1).isEqualTo(2)

        assertThat(result2.content.size).isEqualTo(3)
        assertThat(result2.content.get(0).name).isEqualTo("Spring3")
        assertThat(queryCount2).isEqualTo(2)
        
        assertThat(result3.content.size).isEqualTo(1)
        assertThat(result3.content.get(0).name).isEqualTo("Django")
        assertThat(queryCount3).isEqualTo(2)

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
    
    fun createSeminar(
        instructorEmail: String,
        seminarNme: String = "Spring",
        nParticipant: Int
    ) : SeminarEntity {
        val instructor = testHelper.createInstructor(email = instructorEmail)
        val participants = (1 .. nParticipant).map { testHelper.createParticipant(email= "participant-$it-$instructorEmail") }

        val seminar = testHelper.createSeminar(name=seminarNme, userEntity = instructor)

        participants.forEach {
            testHelper.createUserSeminar(it, seminar)   
        }

        return seminar
    }
}
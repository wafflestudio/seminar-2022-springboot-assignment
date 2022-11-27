package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val seminarService: SeminarService,
    private val userTestHelper: UserTestHelper
) {
    @BeforeEach
    fun clearRepository() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
        userSeminarRepository.deleteAll()
    }

    /*
    * createSeminar()
    */

    @Test
    fun `(createSeminar) 세미나 생성하기`() {
        // Given
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        val createSeminarRequest = CreateSeminarRequest("spring", 30, 6, "19:00", false)

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.createSeminar(instructor.id, createSeminarRequest)
        }

        // Then
        assertThat(response.name).isEqualTo("spring")
        assertThat(response.capacity).isEqualTo(30)
        assertThat(response.count).isEqualTo(6)
        assertThat(response.time).isEqualTo("19:00")
        assertThat(response.online).isEqualTo(false)
        assertThat(response.instructors).hasSize(1)
        assertThat(response.instructors[0].email).isEqualTo("instructor@snu.ac.kr")
        assertThat(response.participants).hasSize(0)
        assertThat(queryCount).isEqualTo(3)
    }

    /*
    * editSeminar()
    */

    @Test
    fun `(editSeminar) 세미나 수정하기`() {
        // Given
        val seminar = createFixtures()
        val seminarId = seminar.id
        val editSeminarRequest = EditSeminarRequest(seminarId, "springboot", 300, 60, "20:00", true)
        val userId = seminar.creatorId

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.editSeminar(userId, editSeminarRequest)
        }

        // Then
        assertThat(response.name).isEqualTo("springboot")
        assertThat(response.capacity).isEqualTo(300)
        assertThat(response.count).isEqualTo(60)
        assertThat(response.time).isEqualTo("20:00")
        assertThat(response.online).isEqualTo(true)
        assertThat(response.instructors).hasSize(1)
        assertThat(response.instructors[0].email).isEqualTo("instructor#0@snu.ac.kr")
        assertThat(response.participants).hasSize(10)
        assertThat(queryCount).isEqualTo(2)
    }


    /*
    * getSeminar()
    */

    @Test
    fun `(getSeminar) seminar id로 세미나 불러오기`() {
        // Given
        val seminar = createFixtures()
        val seminarId = seminar.id

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminar(seminarId)
        }

        // Then
        assertThat(response.name).isEqualTo("spring#0")
        assertThat(response.capacity).isEqualTo(11)
        assertThat(response.count).isEqualTo(6)
        assertThat(response.time).isEqualTo("19:00")
        assertThat(response.online).isEqualTo(false)
        assertThat(response.instructors).hasSize(1)
        assertThat(response.instructors[0].email).isEqualTo("instructor#0@snu.ac.kr")
        assertThat(response.participants).hasSize(10)
        assertThat(queryCount).isEqualTo(1)
    }


    /*
    * searchSeminar()
    */

    @Test
    fun `(searchSeminar) 전체 세미나 불러오기`() {
        // Given
        createFixtures(3)

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.searchSeminar()
        }

        // Then
        assertThat(response).hasSize(3)
        assertThat(response[0].name).isEqualTo("spring#2")
        assertThat(response[1].name).isEqualTo("spring#1")
        assertThat(response[2].name).isEqualTo("spring#0")
        assertThat(queryCount).isEqualTo(1)
    }

    @Test
    fun `(searchSeminar) 특정 string이 포함된 세미나 오래된 순으로 불러오기`() {
        // Given
        createFixtures(3)
        val name = "spring"
        val order = "earliest"

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.searchSeminar(name, order)
        }

        // Then
        assertThat(response).hasSize(3)
        assertThat(response[0].name).isEqualTo("spring#0")
        assertThat(response[1].name).isEqualTo("spring#1")
        assertThat(response[2].name).isEqualTo("spring#2")
        assertThat(queryCount).isEqualTo(1)
    }

    /*
    * joinSeminar()
    */

    @Test
    fun `(joinSeminar) 세미나 수강하기`() {
        // Given
        val seminar = createFixtures()
        val participant = userTestHelper.createParticipant("participant@snu.ac.kr")
        val seminarId = seminar.id
        val userId = participant.id
        val joinSeminarRequest = JoinSeminarRequest("PARTICIPANT")

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.joinSeminar(seminarId, userId, joinSeminarRequest)
        }

        // Then
        assertThat(response.name).isEqualTo("spring#0")
        assertThat(response.capacity).isEqualTo(11)
        assertThat(response.count).isEqualTo(6)
        assertThat(response.time).isEqualTo("19:00")
        assertThat(response.online).isEqualTo(false)
        assertThat(response.instructors).hasSize(1)
        assertThat(response.instructors[0].email).isEqualTo("instructor#0@snu.ac.kr")
        assertThat(response.participants).hasSize(11)
        assertThat(queryCount).isEqualTo(3)
    }

    /*
    * dropSeminar()
    */

    @Test
    fun `(dropSeminar) 세미나 드랍하기`() {
        // Given
        val seminar = createFixtures()
        val seminarId = seminar.id
        val participant = seminar.userSeminars.find { it.role == User.Role.PARTICIPANT }!!.user
        val userId = participant.id


        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.dropSeminar(seminarId, userId)
        }

        // Then
        assertThat(response.name).isEqualTo("spring#0")
        assertThat(response.capacity).isEqualTo(11)
        assertThat(response.count).isEqualTo(6)
        assertThat(response.time).isEqualTo("19:00")
        assertThat(response.online).isEqualTo(false)
        assertThat(response.instructors).hasSize(1)
        assertThat(response.instructors[0].email).isEqualTo("instructor#0@snu.ac.kr")
        assertThat(response.participants).hasSize(10)
        assertThat(response.participants.find { it.email == participant.email }!!.isActive).isEqualTo(
            false
        )
        assertThat(queryCount).isEqualTo(2)
    }

    private fun createFixtures(num: Int = 1, idx: Int = 0): SeminarEntity {
        val participants = (1..10).map { userTestHelper.createParticipant("participant#$it@snu.ac.kr") }
        val seminars = mutableListOf<SeminarEntity>()
        for (i in 0 until num) {
            val instructor = userTestHelper.createInstructor("instructor#$i@snu.ac.kr")

            val seminar = SeminarEntity("spring#$i", 11, 6, LocalTime.parse("19:00"), false, instructor.id)
            val userSeminarList: MutableList<UserSeminarEntity> =
                participants.map {
                    UserSeminarEntity(
                        it,
                        seminar,
                        User.Role.PARTICIPANT,
                        LocalDateTime.now(),
                    )
                } as MutableList<UserSeminarEntity>
            userSeminarList.add(UserSeminarEntity(instructor, seminar, User.Role.INSTRUCTOR, LocalDateTime.now()))
            userSeminarList.forEach { seminar.userSeminars.add(it) }
            seminarRepository.save(seminar)
            seminars.add(seminar)
        }
        return seminars[idx]
    }
}
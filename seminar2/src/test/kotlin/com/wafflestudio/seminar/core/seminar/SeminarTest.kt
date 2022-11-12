package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.system.measureTimeMillis

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val seminarRepository: SeminarRepository,
    private val seminarTestHelper: SeminarTestHelper,
    private val userSeminarRepository: UserSeminarRepository,
    private val userTestHelper: UserTestHelper,
    private val hibernateQueryCounter: HibernateQueryCounter,
) {
    @BeforeEach
    fun beforeEachTest() {
        userTestHelper.deleteAllUser()
        seminarTestHelper.deleteAllSeminar()
        userSeminarRepository.deleteAll()
    }
    
    @Test
    fun `세미나 조회 - 전체 세미나를 조회할 수 있다`() {
        // given
        createFixtures()
        
        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarList("", "")
        }

        // then
        assertThat(result).hasSize(1)
        assertThat(queryCount).isEqualTo(13)
    }
    
    private fun createFixtures() {
        val seminar = seminarTestHelper.createSeminar("seminar#1")
        val seminar2 = seminarTestHelper.createSeminar("seminar#2")
        val instructor = userTestHelper.createInstructor("instructor@email.com")
        val instructorUserSeminar = UserSeminarEntity(user = instructor, seminar = seminar, role = "instructor")
        userSeminarRepository.save(instructorUserSeminar)
        instructor.userSeminars.add(instructorUserSeminar)
        seminar.userSeminars.add(instructorUserSeminar)

        val participants = (1..10).map { userTestHelper.createParticipant("participant#$it@email.com") }
        participants.map {
            val participantUserSeminar = UserSeminarEntity(user = it, seminar = seminar, role = "participant")
            it.userSeminars.add(participantUserSeminar)
            seminar.userSeminars.add(participantUserSeminar)
            userSeminarRepository.save(participantUserSeminar)
        }
    }
}
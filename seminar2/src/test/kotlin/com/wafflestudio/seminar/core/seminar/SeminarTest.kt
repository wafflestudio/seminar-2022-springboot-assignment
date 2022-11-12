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
    fun `전체 세미나를 조회할 수 있다`() {
        // given
        createFixtures()
        
        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getAllSeminar("", "")
        }

        // then
        assertThat(result).hasSize(1)
        assertThat(queryCount).isEqualTo(3)
    }

    @Test
    fun `쿼리 방식에 따라 시간차이가 난다`() {
        // given
        createFixtures()

        // when
        val firstResult = measureTimeMillis { seminarService.getAllSeminar("", "") }
//        val secondResult = measureTimeMillis { seminarService.getSeminarListWithFetchJoin(GetSeminarRequest(null, null)) }

        // then
        println(firstResult)
//        println(secondResult)
//        assertThat(secondResult).isLessThan(firstResult)
    }
    
    private fun createFixtures() {
        val seminar = seminarTestHelper.createSeminar("test seminar")
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
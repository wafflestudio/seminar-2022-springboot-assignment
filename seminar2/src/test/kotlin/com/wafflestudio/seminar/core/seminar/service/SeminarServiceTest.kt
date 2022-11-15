package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.GetSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.system.measureTimeMillis

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,

    private val seminarRepository: SeminarRepository,
    private val userTestHelper: UserTestHelper,
) {

    /**
     *  TODO 세미나를 조회하면:
     *   세미나에 참여중인 유저, 유저 프로필 등 다양한 엔티티를 조회하게 된다.
     *   이 때 우리 의도대로 쿼리가 두 번 나갔는지 확인할 수 있어야 한다.
     */
    @Test
    fun `전체 세미나를 조회할 수 있다`() {
        // given
        createFixtures()

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarList(GetSeminarRequest(null, null))
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
        val firstResult = measureTimeMillis { seminarService.getSeminarList(GetSeminarRequest(null, null)) }
        val secondResult = measureTimeMillis { seminarService.getSeminarListWithFetchJoin(GetSeminarRequest(null, null)) }

        // then
        println(firstResult)
        println(secondResult)
        assertThat(secondResult).isLessThan(firstResult)
    }

    private fun createFixtures() {
        val instructor = userTestHelper.createInstructor("ins@tructor.com")
        val participants = (1..10).map { userTestHelper.createParticipant("par@ticipant#$it.com") }

        val seminar = SeminarEntity.fixture(instructor, capacity = 100)
        participants.map { seminar.addUser(it, UserSeminarEntity.Role.PARTICIPANT) }
        seminarRepository.save(seminar)
    }

}
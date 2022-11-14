package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.maptable.SeminarUserRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest

@SpringBootTest
internal class SeminarQueryCountTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val seminarUserRepository: SeminarUserRepository,
    private val userTestHelper: UserTestHelper,
    private val seminarTestHelper: SeminarTestHelper,
) {

    val httpServletRequest = mock(HttpServletRequest::class.java)

    @Test
    @Transactional // 다음 에러 때문에 annotate: failed to lazily initialize a collection of role: com.wafflestudio.seminar.core.seminar.database.SeminarEntity.seminarUser, could not initialize proxy - no Session
    // 여기에 annotate 하는 경우 쿼리가 1개 (원래보다도 적게) 나와요, SeminarService.kt의 getQuerySeminar에 annotate하는 하면 3 나오는 것 보니까 N+1 problem은 없어 보입니다
    fun `여러 세미나 조회 Query Count`() {
        // given
        val instructor = userTestHelper.createInstructor("inst@ructor.com")
        given(httpServletRequest.getAttribute("email")).willReturn("inst@ructor.com")
        val participants = (0..9).map {
            userTestHelper.createParticipant("part$it@icipant.com")
        }

        val seminar = seminarTestHelper.createSeminar(instructor = instructor)

        participants.forEach {
            val seminarUser = seminarUserRepository.save(
                SeminarUser(seminar, it, Role.Participants)
            )
            seminar.seminarUser.add(seminarUser)
            it.seminarUser.add(seminarUser)
        }

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getQuerySeminar(httpServletRequest, "", "")
        }

        // then
        assertThat(result).hasSize(1)
        assertThat(queryCount).isEqualTo(3)
    }
}
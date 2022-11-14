package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableEntity
import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

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
    
    @Transactional
    fun prepareSeminarList() {
        val instructor = userTestHelper.createInstructor("inst@ructor.com")
        val participants = (0..9).map {
            userTestHelper.createParticipant("part$it@icipant.com")
        }

        val seminar = seminarTestHelper.createSeminar(instructor = instructor)

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
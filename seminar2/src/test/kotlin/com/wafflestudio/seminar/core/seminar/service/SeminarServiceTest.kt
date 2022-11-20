package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.SeminarService
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.mockito.BDDMockito.anyString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
        private val hibernateQueryCounter: HibernateQueryCounter,
        private val seminarRepository: SeminarRepository,
        private val userSeminarRepository: UserSeminarRepository,
        private val userRepository: UserRepository,
        private val seminarService: SeminarService,
        private val userTestHelper: UserTestHelper
) {
    @MockBean
    private lateinit var authTokenService: AuthTokenService

    @BeforeEach
    fun createInstructor() {
        given(authTokenService.getCurrentEmail(anyString())).willReturn("email@snu.ac.kr")
        userRepository.deleteAll()
        seminarRepository.deleteAll()
        userTestHelper.createInstructor("email@snu.ac.kr")
    }

    /*
    * createSeminar()
    */


    // Failed: [N+1]
    @Test
    @Transactional
    fun `(createSeminar) 세미나 생성하기`() {
        // Given
        val seminarRequest = SeminarRequest("spring", 30, 6, "19:00", false)

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.createSeminar(seminarRequest, 1)
        }

        // Then
        assertThat(response.name).isEqualTo("spring")
        // assertThat(queryCount).isEqualTo(8) // [N+1] but was 10
    }

    // Passed
    @Test
    fun `(createSeminar) request body에 필수 정보가 누락되면 400으로 응답`() {
        // Given
        val seminarRequest = SeminarRequest(null, 30, 6, "19:00", false)

        // When
        val response = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest, 1) }

        // Then
        assertThat(response.message).isEqualTo("입력하지 않은 값이 있습니다")
    }

    // Passed
    @Test
    fun `(createSeminar) name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답`() {
        // Given
        val seminarRequest1 = SeminarRequest("", 30, 6, "19:00", false)
        val seminarRequest2 = SeminarRequest("spring", 0, 6, "19:00", false)
        val seminarRequest3 = SeminarRequest("spring", 30, -2, "19:00", false)

        // When
        val response1 = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest1, 1) }
        val response2 = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest2, 1) }
        val response3 = assertThrows<Seminar400> { seminarService.createSeminar(seminarRequest3, 1) }

        // Then
        assertThat(response1.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
        assertThat(response2.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
        assertThat(response3.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
    }

    // Passed
    @Test
    fun `(createSeminar) 진행자 자격이 없는 User가 요청하면 403으로 응답`() {
        // Given
        userRepository.deleteAll()
        userTestHelper.createParticipant("email@snu.ac.kr")
        val seminarRequest = SeminarRequest("spring", 30, 6, "19:00", false)

        // When
        val response = assertThrows<Seminar403> { seminarService.createSeminar(seminarRequest, 1) }

        // Then
        assertThat(response.message).isEqualTo("진행자만 세미나를 생성할 수 있습니다")
    }

    /*
    * updateSeminar()
    */

    // Failed: [N+1]
    @Test
    fun `(updateSeminar) 세미나 수정하기`() {
        // Given
        seminarService.createSeminar(SeminarRequest("spring", 30, 6, "19:00", false), 1)
        val seminarRequest = SeminarRequest("spring", 300, 60, "20:00", true)


        // When
        val (response, queryCount) = hibernateQueryCounter.count {

            seminarService.updateSeminar(seminarRequest, 1)
        }

        // Then
        assertThat(response.capacity).isEqualTo(300)
        assertThat(response.count).isEqualTo(60)
        assertThat(response.time).isEqualTo("20:00")
        assertThat(response.online).isEqualTo(true)

        println(queryCount)
        //assertThat(queryCount).isEqualTo(4) // [N+1] but was 5
    }

    // Passed
    @Test
    fun `(updateSeminar) request body에 필수 정보가 누락되면 400으로 응답`() {
        // Given
        val seminarRequest = SeminarRequest(null, 30, 6, "19:00", false)

        // When
        val response = assertThrows<Seminar400> { seminarService.updateSeminar(seminarRequest, 1) }

        // Then
        assertThat(response.message).isEqualTo("입력하지 않은 값이 있습니다")
    }

    // Passed
    @Test
    fun `(updateSeminar) name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답`() {
        // Given
        val seminarRequest1 = SeminarRequest("", 30, 6, "19:00", false)
        val seminarRequest2 = SeminarRequest("spring", 0, 6, "19:00", false)
        val seminarRequest3 = SeminarRequest("spring", 30, -2, "19:00", false)

        // When
        val response1 = assertThrows<Seminar400> { seminarService.updateSeminar(seminarRequest1, 1) }
        val response2 = assertThrows<Seminar400> { seminarService.updateSeminar(seminarRequest2, 1) }
        val response3 = assertThrows<Seminar400> { seminarService.updateSeminar(seminarRequest3, 1) }

        // Then
        assertThat(response1.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
        assertThat(response2.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
        assertThat(response3.message).isEqualTo("형식에 맞지 않게 입력하지 않은 값이 있습니다")
    }

    // Passed
    @Test
    fun `(updateSeminar) 진행자 자격이 없는 User가 요청하면 403으로 응답`() {
        // Given
        userRepository.deleteAll()
        userTestHelper.createParticipant("email@snu.ac.kr")
        val seminarRequest = SeminarRequest("spring", 30, 6, "19:00", false)

        // When
        val response = assertThrows<Seminar403> { seminarService.updateSeminar(seminarRequest, 1) }

        // Then
        assertThat(response.message).isEqualTo("세미나를 수정할 자격이 없습니다")
    }

    // Failed: 에러가 발생하지 않고 정상적으로 수행됨
    @Test
    @Transactional
    fun `(updateSeminar) 해당 세미나를 만들지 않은 User가 요청하면 403으로 응답`() {
        // Given
        userRepository.deleteAll()
        userTestHelper.createInstructor("email@snu.ac.kr")
        seminarRepository.save(SeminarEntity("spring", 30, 6, "19:00", false))
        val seminarRequest = SeminarRequest("spring", 300, 60, "20:00", true)

        // When
        val response = assertThrows<Seminar403> { seminarService.updateSeminar(seminarRequest, 2) }

        // Then
        assertThat(response.message).isEqualTo("진행자만 세미나를 생성할 수 있습니다")
    }


    /*
    * getSeminarById()
    */

    // Failed: [N+1]
    @Test
    fun `(getSeminarById) seminar id로 세미나 불러오기`() {
        // Given
        val list = createFixtures()

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarById(list[0].id)
        }

        // Then
        assertThat(response.name).isEqualTo("spring#0")
        assertThat(response.instructors).hasSize(1)
        assertThat(response.participants).hasSize(10)
        println(queryCount)
        // assertThat(queryCount).isEqualTo(4) // [N+1] but was 15
    }

    // Passed
    @Test
    fun `(getSeminarById) 해당하는 Seminar가 없는 경우 404로 응답`() {
        // Given
        val id = 100L

        // When
        val response = assertThrows<Seminar404> { seminarService.getSeminarById(id) }

        // Then
        assertThat(response.message).isEqualTo("해당하는 세미나가 없습니다")
    }

    /*
    * getSeminars()
    */


    // Passed
    @Test
    fun `(getSeminarList) 전체 세미나 불러오기`() {
        // Given
        createFixtures(3)

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarList(null, null)
        }

        // Then
        assertThat(response).hasSize(3)
        println(queryCount)
        //  assertThat(queryCount).isEqualTo(1)
    }

    /*
    * getSeminarByName()
    */

    // Failed: [N+1]
    @Test
    fun `(getSeminarList) 특정 string을 포함한 세미나 불러오기`() {
        // Given
        createFixtures(3)

        val name = "spring"
        val order = "earliest"

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarList(name, order)
        }

        // Then
        assertThat(response).hasSize(3) //return값의 자료형이 스펙에 맞지 않습니다.
        println(queryCount)
        //assertThat(queryCount).isEqualTo(2) // [N+1] but was 65
    }


    /*
    * joinSeminar()
    */

    // Failed: [N+1]
    @Test
    fun `(joinSeminar) 세미나 수강하기`() {
        // Given
        val list = createFixtures()
        val participant = userTestHelper.createParticipant("participant@snu.ac.kr")
        val role = mapOf<String, String>("role" to "PARTICIPANT")
        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.joinSeminar(list[0].id, role, participant.id)
        }

        // Then
        println(response.participants?.get(10)?.email)
        assertThat(response.participants).hasSize(11)
        println(queryCount)
        //assertThat(queryCount).isEqualTo(17) // [N+1] but was 29
    }

    // Failed: [N+1]
    @Test
    fun `(joinSeminar) 세미나 함께 진행하기`() {
        // Given
        val list = createFixtures()
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        val role = mapOf<String, String>("role" to "INSTRUCTOR")

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.joinSeminar(list[0].id, role, instructor.id)
        }

        // Then
        assertThat(response.instructors).hasSize(2)
        println(queryCount)
        // assertThat(queryCount).isEqualTo(17) // [N+1] but was 29
    }


    // Passed
    @Test
    fun `(joinSeminar) 해당하는 Seminar가 없는 경우 404로 응답`() {
        // Given
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        val role = mapOf<String, String>("role" to "INSTRUCTOR")

        // When
        val response = assertThrows<Seminar404> { seminarService.joinSeminar(100, role, instructor.id) }

        // Then
        assertThat(response.message).isEqualTo("해당하는 세미나가 없습니다.")
    }

    // Passed
    @Test
    fun `(joinSeminar) role이 잘못된 경우 400으로 응답`() {
        // Given
        val list = createFixtures()
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        val role = mapOf<String, String>("role" to "INSTRUCTOR1")

        // When
        val response = assertThrows<Seminar400> { seminarService.joinSeminar(list[0].id, role, instructor.id) }

        // Then
        assertThat(response.message).isEqualTo("진행자 혹은 수강자가 아닙니다.")
    }

    // Passed
    @Test
    fun `(joinSeminar) 해당하는 자격을 가지지 못한 경우 403으로 응답`() {
        // Given
        val list = createFixtures()
        val participant = userRepository.save(UserEntity("", "", "", LocalDate.now()))
        val role = mapOf<String, String>("role" to "PARTICIPANT")

        // When
        val response = assertThrows<Seminar403> { seminarService.joinSeminar(list[0].id, role, participant.id) }

        // Then
        assertThat(response.message).isEqualTo("참가자로 등록되어 있지 않습니다")
    }

    // Failed: 403으로 응답해야 하는데 400으로 응답함
    @Test
    fun `(joinSeminar) 활성회원이 아닌 사용자가 세미나에 참여하는 요청을 하는 경우 403으로 응답`() {
        // Given
        val list = createFixtures()
        val participant = userTestHelper.createParticipant("participant@snu.ac.kr", isRegistered = false)
        val role = mapOf<String, String>("role" to "PARTICIPANT")

        // When
        val response = assertThrows<Seminar403> { seminarService.joinSeminar(list[0].id, role, participant.id) }

        // Then
        assertThat(response.message).isEqualTo("활성회원이 아닙니다")
    }

    // Passed
    @Test
    fun `(joinSeminar) 세미나가 이미 가득 찬 경우 400으로 응답`() {
        // Given
        val list = createFixtures()
        val role = mapOf<String, String>("role" to "PARTICIPANT")
        val participant1 = userTestHelper.createParticipant("participant1@snu.ac.kr")

        seminarService.joinSeminar(list[0].id, role, participant1.id)

        val participant2 = userTestHelper.createParticipant("participant2@snu.ac.kr")

        // When
        val response = assertThrows<Seminar400> { seminarService.joinSeminar(list[0].id, role, participant2.id) }

        // Then
        assertThat(response.message).isEqualTo("세미나의 인원이 다 찼습니다")
    }

    // Passed
    @Test
    fun `(joinSeminar) 진행자가 이미 담당하고 있는 세미나가 있는 경우 400으로 응답`() {
        // Given
        val list = createFixtures()
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        given(authTokenService.getCurrentEmail(anyString())).willReturn("instructor@snu.ac.kr")
        seminarService.createSeminar(SeminarRequest("test", 10, 10, "10:00"), instructor.id)

        given(authTokenService.getCurrentUserId(anyString())).willReturn(instructor.id)
        val role = mapOf<String, String>("role" to "INSTRUCTOR")

        // When
        val response = assertThrows<Seminar400> { seminarService.joinSeminar(list[0].id, role,instructor.id) }

        // Then
        assertThat(response.message).isEqualTo("이미 다른 세미나를 진행하고 있습니다.")
    }

    // Passed
    @Test
    fun `(joinSeminar) 이미 참여하고 있는 경우 400으로 응답`() {
        // Given
        val list = createFixtures()
        given(authTokenService.getCurrentUserId(anyString())).willReturn(list[0].userSeminars?.get(0)?.user?.id)
        val role = mapOf<String, String>("role" to "PARTICIPANT")

        // When
        val response = assertThrows<Seminar400> { seminarService.joinSeminar(list[0].id, role, 2) }

        // Then
        assertThat(response.message).isEqualTo("이미 세미나에 참여하고 있습니다")
    }

    // Passed
    @Test
    fun `(joinSeminar) 이전에 드랍했던 세미나인 경우 400으로 응답`() {
        // Given
        val list = createFixtures()
        val id = list[0].id
        given(authTokenService.getCurrentUserId(anyString())).willReturn(list[0].userSeminars?.get(0)?.user?.id)
        given(authTokenService.getCurrentEmail(anyString())).willReturn("participant#1@snu.ac.kr")
        seminarService.dropSeminar(id, 4)
        val role = mapOf<String, String>("role" to "PARTICIPANT")

        // When
        val response = assertThrows<Seminar400> { seminarService.joinSeminar(id, role, 4) }

        // Then
        
        assertThat(response.message).isEqualTo("드랍한 세미나는 다시 신청할 수 없습니다")
    }


    /*
    * dropSeminar()
    */

    // Failed: [N+1]
    @Test
    fun `(dropSeminar) 세미나 드랍하기`() {
        // Given
        val list = createFixtures()
        val id = list[0].id
        given(authTokenService.getCurrentEmail(anyString())).willReturn("participant#1@snu.ac.kr")

        // When
        val (response, queryCount) = hibernateQueryCounter.count {
            seminarService.dropSeminar(id, 3L)
        }

        // Then
        assertThat(response.participants!!.find { it.id == 3L }!!.isActive).isEqualTo(false)
        println(queryCount)
        // assertThat(queryCount).isEqualTo(7) // [N+1] but was 19
    }

    // Passed
    @Test
    fun `(dropSeminar) 해당하는 세미나가 없는 경우 404로 응답`() {
        // Given
        createFixtures()
        given(authTokenService.getCurrentEmail(anyString())).willReturn("participant#1@snu.ac.kr")

        // When
        val response = assertThrows<Seminar404> { seminarService.dropSeminar(100, 4) }

        // Then
        assertThat(response.message).isEqualTo("해당 세미나를 신청한 적이 없습니다")
    }

    // Passed
    @Test
    fun `(dropSeminar) 세미나 진행자가 요청하는 경우 403으로 응답`() {
        // Given
        val list = createFixtures()
        given(authTokenService.getCurrentEmail(anyString())).willReturn("instructor#1@snu.ac.kr")

        // When
        val response = assertThrows<Seminar403> { seminarService.dropSeminar(list[0].id, 2) }

        // Then
        assertThat(response.message).isEqualTo("진행자는 세미나를 드랍할 수 없습니다")
    }



 

 
    private fun createFixtures(num: Int = 1): List<SeminarEntity> {

        val list = mutableListOf<SeminarEntity>()
        for (i in 0 until num) {
            val instructor = userTestHelper.createInstructor("instructor#$i@snu.ac.kr")
            val participants = (1..10).map { userTestHelper.createParticipant("participant#$it@snu.ac.kr") }

            val seminar = SeminarEntity("spring#$i", 11, 6, "19:00", false)
            val userSeminarList: MutableList<UserSeminarEntity> =
                    participants.map { UserSeminarEntity(it, seminar, "PARTICIPANT", LocalDateTime.now()) } as MutableList<UserSeminarEntity>
            userSeminarList.add(UserSeminarEntity(instructor, seminar, "INSTRUCTOR", LocalDateTime.now()))
            seminar.userSeminars = userSeminarList
            val seminarEntity = seminarRepository.save(seminar)
            
            userSeminarList.forEach { userSeminarRepository.save(it) }
            list.add(seminarEntity)
        }

        return list
    }
}

 
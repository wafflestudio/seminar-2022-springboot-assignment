package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.join.UserSeminarRepository
import com.wafflestudio.seminar.core.profile.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.dto.SeminarPostRequest
import com.wafflestudio.seminar.core.seminar.dto.SeminarPutRequest
import com.wafflestudio.seminar.core.seminar.dto.SeminarRegisterRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class SeminarServiceImplTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,

    private val seminarTestHelper: SeminarTestHelper,

    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository
) {

    @BeforeEach
    private fun doBefore() {

        // 각 레퍼지토리들에 저장된 row들 삭제함으로써 초기화

        participantProfileRepository.deleteAll()
        instructorProfileRepository.deleteAll()
        userSeminarRepository.deleteAll()
        seminarRepository.deleteAll()
        userRepository.deleteAll()

    }

    /*
    POST /api/vi/seminar 관련 테스트
    테스트 1 - 세미나 생성
    테스트 2 - (예외) 수강생 세미나 생성 불가
    테스트 3 - (예외) 기담당 세미나 존재시, 진행자 또한 세미나 생성 불가
    */

    @Test
    @Transactional
    fun `Test 1-1 세미나를 생성할 수 있다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")

        // when
        val seminarId = seminarService.createSeminarAndReturnSeminarDetail(
            seminarPostRequest = SeminarPostRequest(name = "", capacity = 100, count = 10, time = "01:30", online = false),
            meUser = instructor
        ).id

        // then
        val result = seminarRepository.findByIdOrNull(seminarId)!!.created_user
        assertEquals(result, instructor)
    }

    @Test
    @Transactional
    fun `Test 1-2 수강생은 세미나를 생성할 수 없다`() {
        // given
        val participant = seminarTestHelper.createParticipant("par@ticipant.com")

        // when - then
        val exception = assertThrows<Seminar403> {
            seminarService.createSeminarAndReturnSeminarDetail(
                seminarPostRequest = SeminarPostRequest(
                    name = "", capacity = 100, count = 10,
                    time = "1:30", online = false
                ),
                meUser = participant
            )
        }
        // 에러 메시지를 통해 예외 처리가 제대로 이루어졌는지 재검증
        assertEquals("Not an instructor.", exception.message)
    }

    @Test
    @Transactional
    fun `Test 1-3 이미 맡고 있는 세미나가 있다면 세미나를 생성할 수 없다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor, name = "세미나 하나")

        // when - then
        assertThrows<Seminar400> {
            seminarService.createSeminarAndReturnSeminarDetail(
                seminarPostRequest = SeminarPostRequest(
                    name = "세미나 둘", capacity = 100, count = 10,
                    time = "01:45", online = false
                ),
                meUser = instructor
            )
        }
    }

    /*
    PUT /api/v1/seminar 관련 테스트
    테스트 1 - 본인 담당 세미나 수정
    테스트 2 - (예외) 수정 권한이 없는 세미나 수정 <- 수강생, 혹은 진행자이나 본인 담당 x
     */

    @Test
    @Transactional
    fun `Test 2-1 본인 담당 세미나 정보를 수정할 수 있다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor, name = "세미나")

        // when
        seminarService.modifySeminarAndReturnSeminarDetail(
            // 이름 수정 : 셈이나 -> 세미나
            seminarPutRequest = SeminarPutRequest(id = seminar.id, name = "셈이나"),
            meUser = instructor
        )

        // then
        assertEquals(seminarRepository.findByIdOrNull(seminar.id)!!.name, "셈이나")
    }

    @Test
    @Transactional
    fun `Test 2-2 수강생은 세미나 정보를 수정할 수 없다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor, name = "세미나")
        val participant = seminarTestHelper.createParticipant("par@ticipant.com")
            .also {
                seminarTestHelper.joinSeminar(it, seminar, PARTICIPANT)
            }

        // when-then 
        assertThrows<Seminar403> {
            seminarService.modifySeminarAndReturnSeminarDetail(
                // 이름 수정 : 셈이나 -> 세미나
                seminarPutRequest = SeminarPutRequest(id = seminar.id, name = "셈이나"),
                meUser = participant
            )
        }
    }

    @Test
    @Transactional
    fun `Test 2-3 본인 담당이 아니면 진행자도 세미나 정보를 수정할 수 없다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor, name = "세미나")

        val instructor2 = seminarTestHelper.createInstructor("ins@tructor2.com")

        // when - then
        val exception = assertThrows<Seminar403> {
            seminarService.modifySeminarAndReturnSeminarDetail(
                // 이름 수정 : 세미나 -> 셈이나
                seminarPutRequest = SeminarPutRequest(id = seminar.id, name = "셈이나"),
                meUser = instructor2
            )
        }
        // 에러 메시지를 통해 예외 처리가 제대로 이루어졌는지 재검증
        assertEquals("Not a creator of seminar.", exception.message)
    }

    /*
    GET /api/v1/seminar/{seminarId} 관련 테스트
    테스트 1 - 세미나 정보 조회 & 쿼리가 한 번만 날려지는지 확인
    테스트 2 - (예외) 해당하는 세미나가 없을 때 404 에러
     */

    @Test
    @Transactional
    fun `Test 3-1 세미나 id에 해당하는 세미나 정보를 조회할 수 있다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor)

        // when
        val (foundSeminar, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarDetailById(seminar.id)
        }

        // then
        // 쿼리 실행 개수 확인 - JPA N+1 문제 확인
        assertThat(queryCount).isEqualTo(1)
        // 그런 다음, 본 함수의 실행이 잘 이루어졌는지 확인
        assertEquals(foundSeminar, seminar)
    }

    @Test
    @Transactional
    fun `Test 3-2 존재하지 않는 세미나 id로 세미나 조회시 404로 응답한다`() {
        // given
        // 아무것도 생성하지 않기

        // when - then
        val seminarId = 1L
        val exception = assertThrows<Seminar404> {
            seminarService.getSeminarDetailById(seminarId)
        }
        assertEquals("Seminar $seminarId does not exists.", exception.message)
    }

    /*
    GET /api/v1/seminar?name={name}&order={order} 관련 테스트
    테스트 1 - name, order 공란 시 전체 세미나 정보 조회 (JPA N+1 문제 확인)
    테스트 2 - 특정 name을 포함하는 세미나 정보 조회
    테스트 3 - order을 통해 정렬된 세미나 정보 조회
     */

    @Test
    @Transactional
    fun `Test 4-1 전체 세미나를 조회할 수 있다`() {
        // given
        val num = 4
        val instructors = (1..num).map { seminarTestHelper.createInstructor("ins@truction#$it.com") }
        instructors.forEach {
            seminarTestHelper.createSeminar(it, name = "세미나 ${it.id}")
        }

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarListQueriedByNameAndOrder("", "")
        }

        // then
        assertThat(queryCount).isEqualTo(3)
        assertThat(result.size).isEqualTo(num)
    }

    @Test
    @Transactional
    fun `Test 4-2 이름에 특정 단어를 포함하는 세미나를 조회할 수 있다`() {
        // given
        val num = 2
        val nameList = listOf("뚱인데요", "와플스튜디오")
        val instructors = (1..num).map { seminarTestHelper.createInstructor("ins@truction#$it.com") }
        instructors.forEachIndexed { idx, instructor ->
            seminarTestHelper.createSeminar(instructor, name = nameList[idx])
        }

        // when
        val result = seminarService.getSeminarListQueriedByNameAndOrder(name = "와플", order = "")

        // then
        // result의 사이즈가 1인지 확인
        assertThat(result.size).isEqualTo(1)
        // result에 포함된 세미나가 정말로 이름에 "와플"을 포함하고 있는지 확인
        assertThat(result[0].name.contains("와플")).isTrue
    }

    @Test
    @Transactional
    fun `Test 4-3 order을 통해 정렬된 상태로 세미나 정보를 조회할 수 있다`() {
        // given
        // instructor 2명 - 각각 세미나 하나씩 담당
        val num = 2
        val instructors = (1..num).map { seminarTestHelper.createInstructor("ins@truction#$it.com") }
        instructors.forEach {
            seminarTestHelper.createSeminar(it, name = "세미나 ${it.id}")
        }

        // when
        val result = seminarService.getSeminarListQueriedByNameAndOrder(name = "세미나", order = "earliest")
            .flatMap { it.instructors.map { it.joinedAt } }

        // then - 정렬 순서 지켰는지 확인
        assertThat(result[0].isBefore(result[1])).isTrue
    }

    /*
    POST /api/v1/seminar/{seminar_id}/user/
    테스트 1 - 수강생, 세미나 참여
    테스트 2 - 진행자, 세미나 참여
    테스트 3 - (예외) 수강생 기 드랍 강좌 재수강 요청
    테스트 4 - (예외) 담당하고 있는 세미나가 있는 진행자의 세미나 참여 요청
    */

    @Test
    @Transactional
    fun `Test 5-1 수강생은 세미나에 참여할 수 있다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor)

        val participant = seminarTestHelper.createParticipant("par@ticipant.com")

        // when
        seminarService.attendUserToSeminarAndReturnSeminarDetail(
            seminarId = seminar.id,
            seminarRegisterRequest = SeminarRegisterRequest("participant"),
            meUser = participant
        )

        // then
        val foundParticipant = userSeminarRepository
            .findAllBySeminarAndRole(seminar, "participant")
            .find { it.user.id == participant.id }
        assertThat(foundParticipant).isNotNull
    }

    @Test
    @Transactional
    fun `Test 5-2 진행자도 세미나에 참여할 수 있다`() {
        // given 
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor, name = "세미나")

        val instructor2 = seminarTestHelper.createInstructor("ins@tructor2.com")

        // when
        seminarService.attendUserToSeminarAndReturnSeminarDetail(
            seminarId = seminar.id,
            seminarRegisterRequest = SeminarRegisterRequest("instructor"),
            meUser = instructor2
        )

        // then
        val foundInstructor = userSeminarRepository
            .findAllBySeminarAndRole(seminar, "instructor")
            .find { it.user.id == instructor2.id }
        assertThat(foundInstructor).isNotNull
    }

    @Test
    @Transactional
    fun `Test 5-3 이미 드랍한 강좌는 재수강할 수 없다`() {
        // given 
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor, name = "세미나")

        val participant = seminarTestHelper.createParticipant("par@ticipant.com")
        seminarTestHelper.joinSeminar(participant, seminar, "participant", false)

        // when - then
        assertThrows<Seminar400> {
            seminarService.attendUserToSeminarAndReturnSeminarDetail(
                seminarId = seminar.id,
                seminarRegisterRequest = SeminarRegisterRequest("participant"),
                meUser = participant
            )
        }
    }

    @Test
    @Transactional
    fun `Test 5-4 담당하고 있는 강좌가 있는 진행자는 세미나에 참여할 수 없다`() {
        // given
        val instructor1 = seminarTestHelper.createInstructor("ins@tructor1.com")
        val seminar1 = seminarTestHelper.createSeminar(instructor = instructor1)

        val instructor2 = seminarTestHelper.createParticipant("par@ticipant2.com")
        val seminar2 = seminarTestHelper.createSeminar(instructor = instructor2)

        // when - then
        val exception = assertThrows<Seminar400> {
            seminarService.attendUserToSeminarAndReturnSeminarDetail(
                seminarId = seminar1.id,
                seminarRegisterRequest = SeminarRegisterRequest("participant"),
                meUser = instructor2
            )
        }
        assertEquals("Already instructor in other seminar", exception.message)
    }

    /*
    DELETE /api/v1/seminar/{seminar_id}/user
    테스트 1 - 수강생 세미나 드랍
    테스트 2 - (예외) 진행자의 세미나 드랍 요청
     */

    @Test
    @Transactional
    fun `Test 6-1 수강생은 본인이 듣고 있는 세미나를 드랍할 수 있다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor)

        val participant = seminarTestHelper.createParticipant("par@ticipant.com")
        seminarTestHelper.joinSeminar(participant, seminar, "participant")

        // when
        seminarService.dropUserFromSeminar(seminar.id, participant)

        // then
        val foundEntity = userSeminarRepository
            .findAllBySeminarAndRole(seminar, "participant")
            .find { it.user.id == participant.id }!!
        assertThat(foundEntity.isActive).isFalse
    }

    @Test
    @Transactional
    fun `Test 6-2 진행자는 본인의 담당 강좌를 드랍할 수 없다`() {
        // given
        val instructor = seminarTestHelper.createInstructor("ins@tructor.com")
        val seminar = seminarTestHelper.createSeminar(instructor = instructor)

        // when - then
        assertThrows<Seminar403> {
            seminarService.dropUserFromSeminar(seminar.id, instructor)
        }
    }

}